package com.talenArena.SafeZone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Deshabilitar CSRF para APIs REST
                .authorizeHttpRequests(auth -> auth
                        // API 1: Location Verification
                        .requestMatchers("/api/location-verification/**").permitAll()

                        // API 2: SIM Swap
                        .requestMatchers("/api/sim-swap/**").permitAll()

                        // Swagger UI y documentación
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // H2 Console (solo desarrollo)
                        .requestMatchers("/h2-console/**").permitAll()

                        // Todo lo demás
                        .anyRequest().permitAll()
                )
                .httpBasic(AbstractHttpConfigurer::disable) // Deshabilitar autenticación HTTP Basic
                .formLogin(AbstractHttpConfigurer::disable); // Deshabilitar form login

        return http.build();
    }
}

