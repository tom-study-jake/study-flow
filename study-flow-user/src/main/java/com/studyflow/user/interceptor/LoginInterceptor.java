package com.studyflow.user.interceptor;

import com.studyflow.utils.JwtUtils;
import com.studyflow.utils.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 从 Authorization 头获取 token
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            response.setStatus(401);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"success\":false,\"message\":\"未登录\"}");
            return false;
        }

        // 解析 token
        String token = auth.substring(7);
        Long userId = JwtUtils.parseUserId(token);
        if (userId == null) {
            response.setStatus(401);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"success\":false,\"message\":\"token无效或已过期\"}");
            return false;
        }

        // 存入 ThreadLocal
        UserHolder.setUserId(userId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
        UserHolder.remove();
    }
}
