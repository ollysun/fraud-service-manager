package com.etz.fraudeagleeyemanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.etz.fraudeagleeyemanager.constant.AppConstant;

import javax.validation.constraints.NotNull;

@Configuration
public class WebCorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping(AppConstant.ALL_LINKS)
                        .allowedOrigins(AppConstant.ALL)
                        .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
                        .allowedHeaders(AppConstant.ALL);
            }
        };

    }
}
