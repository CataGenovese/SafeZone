package com.talenArena.SafeZone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                // Auth endpoints
                .requestMatchers("/api/auth/**").permitAll()
                // API endpoints
                .requestMatchers("/api/location-verification/**").permitAll()
                .requestMatchers("/api/sim-swap/**").permitAll()
                .requestMatchers("/api/kyc-fill-in/**").permitAll()
                // Swagger UI y documentación
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                // Actuator
                .requestMatchers("/actuator/**").permitAll()
                // H2 Console
                .requestMatchers("/h2-console/**").permitAll()

                // Todos los demás requieren autenticación
                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }
}

