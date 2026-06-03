package com.cws.cwslife.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Gallery images
        registry.addResourceHandler("/gallery/**")
                .addResourceLocations("file:./gallery/");

        // Events images
        registry.addResourceHandler("/uploads/events/**")
                .addResourceLocations("file:uploads/events/");
        
        // Social Contribution Images
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}