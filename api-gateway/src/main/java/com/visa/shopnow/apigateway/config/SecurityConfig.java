package com.visa.shopnow.apigateway.config;

import javax.crypto.spec.SecretKeySpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/v1/users/register", "/api/v1/users/login").permitAll()
                        // Protected endpoints - require authentication with token
                        .pathMatchers("/api/v1/products/**").authenticated()
                        // More granular authorization:
                        // .pathMatchers("/api/v1/products/admin/**").hasRole("ADMIN") // Example for ADMIN role
                        .anyExchange().authenticated() // All other requests must be authenticated
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtSpec -> jwtSpec.jwtDecoder(jwtDecoder())) // Use default JWT decoder for symmetric key
                );
        return http.build();
    }

    @Bean
    public org.springframework.security.oauth2.jwt.ReactiveJwtDecoder jwtDecoder() {
        String secretKey = "your-jwt-secret-that-is-at-least-256bit-long"; // same as user service

        // For symmetric keys (HMACSHA256), use MacAlgorithm
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), MacAlgorithm.HS256.getName());

        return NimbusReactiveJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }
}
