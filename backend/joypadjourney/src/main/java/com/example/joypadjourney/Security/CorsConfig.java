package com.example.joypadjourney.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000"); // URL frontend
        configuration.addAllowedMethod("*"); // Mengizinkan semua metode (GET, POST, dll)
        configuration.addAllowedHeader("*"); // Mengizinkan semua header
        configuration.addExposedHeader("Authorization"); // Membiarkan header Authorization digunakan
        configuration.setAllowCredentials(true); // Mengizinkan pengiriman cookie/token

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Berlaku untuk semua endpoint
        return source;
    }
}
