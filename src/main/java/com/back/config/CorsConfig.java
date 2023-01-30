package com.back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域问题
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //是否发送cookie
                .allowCredentials(true)
                //放行那些原始域
                .allowedOriginPatterns("*")
                .allowedMethods(new String[]{"GET","POST","PUT","DELETE"})
                .allowedHeaders("*")
                .exposedHeaders("*");
    }
}
