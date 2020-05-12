package com.example.questionnaire_backend.config;

import com.example.questionnaire_backend.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/api/login")
                .excludePathPatterns("/api/logout")
                .excludePathPatterns("/api/register")
                .excludePathPatterns("/api/register/namecheck")
                .excludePathPatterns("/api/register/emailcheck")
                .excludePathPatterns("/api/islogin")
                .excludePathPatterns("/api/resolve")
                .excludePathPatterns("/api/submit");
    }
}
