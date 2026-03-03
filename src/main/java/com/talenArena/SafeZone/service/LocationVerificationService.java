package com.talenArena.SafeZone.service;

import com.talenArena.SafeZone.dto.*;
import io.netty.resolver.DefaultAddressResolverGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Service
public class LocationVerificationService {

    private static final Logger log = LoggerFactory.getLogger(LocationVerificationService.class);

    @Value("${location.verification.api.url}")
    private String apiUrl;

    @Value("${location.verification.rapidapi.host}")
    private String rapidApiHost;

    @Value("${location.verification.rapidapi.key}")
    private String rapidApiKey;

    @Value("${location.verification.mock.enabled:false}")
    private boolean mockEnabled;

    public String verifyLocation(LocationVerificationRequest request) {
        log.info("Verificando ubicación para: {}", request);

        HttpClient httpClient = HttpClient.create()
                .resolver(DefaultAddressResolverGroup.INSTANCE)
                .responseTimeout(Duration.ofSeconds(10));

        WebClient webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader("x-rapidapi-key", rapidApiKey)
                .defaultHeader("x-rapidapi-host", rapidApiHost)
                .defaultHeader("Content-Type", "application/json")
                .build();

        try {
            String response = webClient.post()
                    .uri("/verify")
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(status -> status.isError(), clientResponse ->
                            clientResponse.bodyToMono(String.class)
                                    .map(body -> {
                                        log.error("Error de la API - Status: {}, Body: {}", clientResponse.statusCode(), body);
                                        return new RuntimeException("API Error [" + clientResponse.statusCode() + "]: " + body);
                                    })
                    )
                    .bodyToMono(String.class)
                    .block();

            log.info("Respuesta de Location Verification: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error al verificar ubicación: {}", e.getMessage(), e);
            throw new RuntimeException("Error en la verificación de ubicación", e);
        }
    }

    public LocationLastResponse getLastLocation(LocationLastRequest request) {
        if (mockEnabled) {
            LocationLastResponse response = new LocationLastResponse();
            response.setLastLocationTime("2026-03-02T11:40:59.969867Z");
            Area area = new Area();
            area.setAreaType("CIRCLE");
            Center center = new Center();
            center.setLatitude(47.48627616952785);
            center.setLongitude(19.07915612501993);
            area.setCenter(center);
            area.setRadius(1000);
            response.setArea(area);
            return response;
        }
        // Aquí iría la llamada real a la API externa si no es mock
        return null;
    }
}
