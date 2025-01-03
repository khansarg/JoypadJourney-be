package com.example.joypadjourney.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // Bean untuk password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Konfigurasi utama SecurityFilterChain
    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // Aktifkan CORS
        .cors(withDefaults())
        // Nonaktifkan CSRF untuk API
        .csrf(csrf -> csrf.disable())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        // Konfigurasi otorisasi permintaan
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/api/customer/register").permitAll()
            .requestMatchers("/api/auth/login").permitAll()
            .requestMatchers("/api/customer/**").hasAuthority("Customer")
            .requestMatchers("/api/admin/**").hasAuthority("Admin")
            .anyRequest().permitAll()
        )
        // Gunakan otentikasi berbasis HTTP Basic (untuk API)
        .httpBasic(withDefaults())
        // Konfigurasi logout
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login")
            .permitAll()
        );

    return http.build();
}

}
