package com.talenArena.SafeZone.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class NetworkAsCodeConfig {

    @Value("${network.as.code.api.url}")
    private String apiUrl;

    @Value("${network.as.code.token}")
    private String token;

    @Bean
    public WebClient networkAsCodeWebClient() {
        return WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + token)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}

