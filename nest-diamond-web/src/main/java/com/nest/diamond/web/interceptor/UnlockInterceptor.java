package com.nest.diamond.web.interceptor;

import com.nest.diamond.web.anno.UnLock;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;

public class UnlockInterceptor implements HandlerInterceptor {

    @Value("${nest_diamond.unlock.password}")
    private String unLockPassword;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            if (method.isAnnotationPresent(UnLock.class)) {
                String unlockPass = request.getParameter("unlockPassword"); // 假设密码参数名为 unlockPass
                if (!unLockPassword.equals(unlockPass)) {
                    throw new IllegalArgumentException("解锁密码不正确");
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 拦截后处理
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求处理完毕后的处理
    }
}
