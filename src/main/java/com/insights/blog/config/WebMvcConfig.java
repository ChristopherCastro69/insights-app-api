package com.insights.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Adjust this path according to your API endpoints
                .allowedOrigins("http://localhost:5173", "http://localhost:8080", "http://localhost:5174", "https://insights-blog-production.up.railway.app/","https://insights-blog-production.up.railway.app") // Allow requests from this origin
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow these HTTP methods
                .allowedHeaders("*")
                .exposedHeaders("Authorization"); // Expose Authorization header to client
    }
}
