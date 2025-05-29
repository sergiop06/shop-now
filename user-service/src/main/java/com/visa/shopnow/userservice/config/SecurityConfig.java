package com.visa.shopnow.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1. Disable CSRF (Cross-Site Request Forgery) protection
        // Common for stateless REST APIs using tokens (like JWT) instead of sessions.
        http.csrf(AbstractHttpConfigurer::disable)
                // 2. Authorize (define access rules for) HTTP requests
                .authorizeHttpRequests(authorize -> authorize
                        // Permit all access to specific endpoints:
                        .requestMatchers("/api/v1/users/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/login").permitAll()
                        .requestMatchers("/api/v1/users/{id}").permitAll()
                        .requestMatchers("/api/v1/users").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users/{id}/password").permitAll()

                        // Permit access to Swagger/OpenAPI documentation URLs
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Any other request (not explicitly permitted above) must be authenticated
                        .anyRequest().authenticated()
                );
        return http.build(); // Build and return the security filter chain
    }


}
