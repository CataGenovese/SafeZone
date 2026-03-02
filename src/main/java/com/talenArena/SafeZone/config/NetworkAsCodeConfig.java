package com.talenArena.SafeZone.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import io.netty.resolver.DefaultAddressResolverGroup;
import java.time.Duration;

@Configuration
public class NetworkAsCodeConfig {

    @Value("${network.as.code.api.url}")
    private String apiUrl;

    @Value("${network.as.code.token}")
    private String token;

    @Bean
    public WebClient networkAsCodeWebClient() {
        HttpClient httpClient = HttpClient.create()
                .resolver(DefaultAddressResolverGroup.INSTANCE)
                .responseTimeout(Duration.ofSeconds(10));

        return WebClient.builder()
                .baseUrl(apiUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader("x-rapidapi-key", token)
                .defaultHeader("x-rapidapi-host", "network-as-code.nokia.rapidapi.com")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
