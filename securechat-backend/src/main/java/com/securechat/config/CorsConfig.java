package com.securechat.config;

/*
 * Rôle :
 * Configurer CORS pour permettre au frontend React ou React Native
 * de communiquer avec le backend.
 *
 * À faire :
 * - Autoriser localhost frontend
 * - Autoriser méthodes GET, POST, PUT, DELETE
 * - Autoriser header Authorization
 *
 * Couche :
 * Config
 */

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedOrigins(
                                "http://localhost:3000",
                                "http://localhost:5500",
                                "http://127.0.0.1:5500",
                                "http://localhost:5173"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}



