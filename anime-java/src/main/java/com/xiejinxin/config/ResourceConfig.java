package com.xiejinxin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将本地文件夹映射到 /videos/** 路径
        registry.addResourceHandler("/videos/**")
                .addResourceLocations("file:D:/anime_videos/"); // Windows系统
                // .addResourceLocations("file:/home/anime_videos/"); // Linux系统
    }
} 