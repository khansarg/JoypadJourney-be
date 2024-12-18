package com.example.joypadjourney.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Bean untuk password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Konfigurasi utama SecurityFilterChain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Nonaktifkan CSRF untuk API
            .csrf(csrf -> csrf.disable())
            
            // Konfigurasi otorisasi permintaan
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/customer/register", "/login", "/logout").permitAll() // Endpoint yang diizinkan tanpa login
                .anyRequest().permitAll() // Endpoint lain memerlukan otentikasi
            )
            
            // Gunakan otentikasi berbasis HTTP Basic (untuk API)
            .httpBasic(withDefaults())

            // Konfigurasi logout
            .logout(logout -> logout
                .logoutUrl("/logout") // URL untuk logout
                .logoutSuccessUrl("/login") // Arahkan ke halaman ini setelah logout berhasil
                .permitAll()
            );

        return http.build();
    }
}
