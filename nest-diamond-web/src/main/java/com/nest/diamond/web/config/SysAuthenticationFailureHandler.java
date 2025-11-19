package com.nest.diamond.web.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class SysAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private ConcurrentMap<String, AtomicInteger> userFailCounterMap = new ConcurrentHashMap<>();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        // 自定义认证失败处理逻辑，例如记录密码失败次数、锁定用户等
        // 可以使用 request.getParameter("username") 获取用户名
        // 使用适当的数据存储方式（数据库、缓存等）来实现密码失败次数的记录和用户锁定
        String userName = request.getParameter("username");
        if(userFailCounterMap.containsKey(userName)){
            userFailCounterMap.get(userName).incrementAndGet();
        }else{
            userFailCounterMap.put(userName, new AtomicInteger());
        }
        int failCount = userFailCounterMap.get(userName).get();
        if(failCount > 100){
            log.error("失败次数超过100次，不允许再登录", failCount);
            response.sendRedirect("/login?error");
        }
        response.sendRedirect("/login?error");
    }
}
