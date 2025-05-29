package com.visa.shopnow.apigateway.config;

import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.POST, "/api/v1/users", "/api/v1/users/login").permitAll()
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

        // For symmetric keys (HMACSHA256), use MacAlgorithm
        SecretKeySpec secretKeySpec = new SecretKeySpec(jwtSecret.getBytes(), MacAlgorithm.HS256.getName());

        return NimbusReactiveJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }
}
