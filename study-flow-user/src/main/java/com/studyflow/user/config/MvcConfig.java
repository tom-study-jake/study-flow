package com.studyflow.user.config;

import com.studyflow.user.interceptor.LoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class MvcConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**")                    // 拦截所有 /api/ 开头的请求
                .excludePathPatterns("/api/auth/**")           // 放行登录/注册
                .excludePathPatterns("/api/internal/**");      // 放行 Feign 内部调用
    }
}
