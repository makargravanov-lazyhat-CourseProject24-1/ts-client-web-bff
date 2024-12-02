package ru.jetlabs.ts.clientwebbff.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173", "https://tour.lazyhat.ru")
                .allowedMethods("*")
                .allowedHeaders("Cookie", "Set-Cookie")
                .allowCredentials(true);
    }
}
