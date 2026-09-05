package com.danceflow.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AdminOperationLogInterceptor interceptor;
    public WebMvcConfig(AdminOperationLogInterceptor interceptor) { this.interceptor = interceptor; }
    @Override public void addInterceptors(InterceptorRegistry registry) { registry.addInterceptor(interceptor); }
}
