package com.nest.diamond.web.aspect;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.nest.diamond.model.domain.AccessLog;
import com.nest.diamond.service.AccessLogService; // 改为你实际的Service包路径
import com.nest.diamond.web.anno.LogAccess;
import com.nest.diamond.common.annos.LogMask; // 引入刚才定义的注解
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private AccessLogService accessLogService;

    // 专门用于日志记录的 ObjectMapper
    private final ObjectMapper logObjectMapper;

    public LogAspect() {
        this.logObjectMapper = new ObjectMapper();
        // 注册自定义模块，用于处理 @LogMask
        this.logObjectMapper.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) {
                super.setupModule(context);
                context.addBeanSerializerModifier(new BeanSerializerModifier() {
                    @Override
                    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
                        for (BeanPropertyWriter writer : beanProperties) {
                            // 检查字段上是否有 @LogMask 注解
                            LogMask annotation = writer.getAnnotation(LogMask.class);
                            if (annotation != null) {
                                // 如果有，替换为掩码序列化器
                                writer.assignSerializer(new JsonSerializer<Object>() {
                                    @Override
                                    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                                        // 写入 ******
                                        gen.writeString(annotation.value());
                                    }
                                });
                            }
                        }
                        return beanProperties;
                    }
                });
            }
        });
    }

    @Around("@annotation(com.nest.diamond.web.anno.LogAccess)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        LogAccess logAccess = method.getAnnotation(LogAccess.class);

        // === 新增：限流检查逻辑 ===
        if (logAccess != null && logAccess.dailyLimit() > 0) {
            checkLimit(logAccess.value(), logAccess.dailyLimit());
        }
        // ========================

        long beginTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = point.proceed();
        } finally {
            try {
                long time = System.currentTimeMillis() - beginTime;
                saveLog(point, result, time);
            } catch (Exception e) {
                log.error("AccessLog save failed", e);
            }
        }
        return result;
    }

    private void saveLog(ProceedingJoinPoint point, Object result, long time) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        AccessLog accessLog = new AccessLog();

        LogAccess logAccess = method.getAnnotation(LogAccess.class);
        if (logAccess != null) {
            accessLog.setModule(logAccess.value());
        }

        String className = point.getTarget().getClass().getSimpleName();
        String methodName = signature.getName();
        accessLog.setMethodName(className + "." + methodName);

        // 使用 logObjectMapper 序列化参数，敏感字段会自动变 ******
        Object[] args = point.getArgs();
        try {
            if (args != null && args.length > 0) {
                accessLog.setRequestParams(logObjectMapper.writeValueAsString(args));
            }
        } catch (Exception e) {
            log.warn("Parse args failed", e);
        }

        // 使用 logObjectMapper 序列化返回值，敏感字段会自动变 ******
        try {
            if (result != null) {
                accessLog.setResponseData(logObjectMapper.writeValueAsString(result));
            }
        } catch (Exception e) {
            log.warn("Parse result failed", e);
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request != null) {
            accessLog.setIpAddress(getIpAddress(request));
        }

        accessLog.setExecutionTime(time);
        accessLog.setCreateTime(new Date());

        accessLogService.insert(accessLog);
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    private void checkLimit(String module, int limit) {
        // 1. 获取当前请求 IP
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = "unknown";
        if (request != null) {
            ip = getIpAddress(request);
        }

        // 2. 查询数据库统计
        long count = accessLogService.countToday(module);

        // 3. 判断是否超限
        if (count >= limit) {
            log.warn("IP [{}] accessed module [{}] {} times today, limit is {}. Access denied.", ip, module, count, limit);
            throw new RuntimeException("今日访问次数已达上限(" + limit + "次)，请明天再试");
        }
    }
}