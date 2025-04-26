package org.example.untitled_testai.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // 你也可以写 "/**"
                .allowedOrigins("http://localhost:5173") // 允许前端地址
                .allowedMethods("*") // 允许所有请求方式：GET, POST, etc.
                .allowedHeaders("*") // 允许所有请求头
                .allowCredentials(true); // 允许携带 Cookie（如果有）
    }
}