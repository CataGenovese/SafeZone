package com.talenArena.SafeZone.service;

import com.talenArena.SafeZone.models.geofencing.CreateGeofencingSubscriptionRequest;
import com.talenArena.SafeZone.models.geofencing.GeofencingSubscription;
import com.talenArena.SafeZone.models.geofencing.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class GeofencingService {

    private static final String HEADER_RAPIDAPI_KEY = "X-Rapidapi-Key";
    private static final String HEADER_RAPIDAPI_HOST = "X-Rapidapi-Host";
    private static final String RAPIDAPI_HOST_VALUE = "network-as-code.nokia.rapidapi.com";

    private final WebClient webClient;
    private final boolean mockEnabled;

    @Value("${network.as.code.token}")
    private String rapidApiKey;

    public GeofencingService(
            @Value("${network.as.code.api.url}") String baseUrl,
            @Value("${network.as.code.mock.enabled:false}") boolean mockEnabled) {

        this.mockEnabled = mockEnabled;
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<GeofencingSubscription> createSubscription(CreateGeofencingSubscriptionRequest request) {
        if (mockEnabled) {
            log.info("Modo mock habilitado. Simulando creacion de suscripcion de geofencing");
            return Mono.just(createMockSubscription(request));
        }

        log.info("Creando suscripcion de geofencing para: {}", request);

        return webClient.post()
                .uri("/geofencing-subscriptions/v0.3/subscriptions")
                .header(HEADER_RAPIDAPI_KEY, rapidApiKey)
                .header(HEADER_RAPIDAPI_HOST, RAPIDAPI_HOST_VALUE)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GeofencingSubscription.class)
                .doOnSuccess(response -> log.info("Suscripcion creada exitosamente: {}", response))
                .doOnError(error -> log.error("Error al crear suscripcion de geofencing", error));
    }

    public Mono<GeofencingSubscription> getSubscription(String subscriptionId) {
        if (mockEnabled) {
            log.info("Modo mock habilitado. Simulando obtencion de suscripcion: {}", subscriptionId);
            return Mono.just(createMockSubscription(null));
        }

        log.info("Obteniendo suscripcion de geofencing: {}", subscriptionId);

        return webClient.get()
                .uri("/geofencing-subscriptions/v0.3/subscriptions/{subscriptionId}", subscriptionId)
                .header(HEADER_RAPIDAPI_KEY, rapidApiKey)
                .header(HEADER_RAPIDAPI_HOST, RAPIDAPI_HOST_VALUE)
                .retrieve()
                .bodyToMono(GeofencingSubscription.class)
                .doOnSuccess(response -> log.info("Suscripcion obtenida: {}", response))
                .doOnError(error -> log.error("Error al obtener suscripcion de geofencing", error));
    }

    public Mono<Void> deleteSubscription(String subscriptionId) {
        if (mockEnabled) {
            log.info("Modo mock habilitado. Simulando eliminacion de suscripcion: {}", subscriptionId);
            return Mono.empty();
        }

        log.info("Eliminando suscripcion de geofencing: {}", subscriptionId);

        return webClient.delete()
                .uri("/geofencing-subscriptions/v0.3/subscriptions/{subscriptionId}", subscriptionId)
                .header(HEADER_RAPIDAPI_KEY, rapidApiKey)
                .header(HEADER_RAPIDAPI_HOST, RAPIDAPI_HOST_VALUE)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(response -> log.info("Suscripcion eliminada exitosamente"))
                .doOnError(error -> log.error("Error al eliminar suscripcion de geofencing", error));
    }

    public Mono<GeofencingSubscription[]> listSubscriptions() {
        if (mockEnabled) {
            log.info("Modo mock habilitado. Simulando listado de suscripciones");
            return Mono.just(new GeofencingSubscription[]{createMockSubscription(null)});
        }

        log.info("Listando todas las suscripciones de geofencing");

        return webClient.get()
                .uri("/geofencing-subscriptions/v0.3/subscriptions")
                .header(HEADER_RAPIDAPI_KEY, rapidApiKey)
                .header(HEADER_RAPIDAPI_HOST, RAPIDAPI_HOST_VALUE)
                .retrieve()
                .bodyToMono(GeofencingSubscription[].class)
                .doOnSuccess(response -> log.info("Suscripciones obtenidas: {}", response != null ? response.length : 0))
                .doOnError(error -> log.error("Error al listar suscripciones de geofencing", error));
    }

    private GeofencingSubscription createMockSubscription(CreateGeofencingSubscriptionRequest request) {
        GeofencingPoint center = GeofencingPoint.builder()
                .latitude(40.4168)
                .longitude(-3.7038)
                .build();

        GeofencingCircle circle = GeofencingCircle.builder()
                .center(center)
                .radius(1000)
                .build();

        GeofencingArea area = GeofencingArea.builder()
                .areaType("CIRCLE")
                .circle(circle)
                .build();

        GeofencingDeviceInfo deviceInfo = GeofencingDeviceInfo.builder()
                .phoneNumber("+34600000000")
                .build();

        GeofencingSubscriptionDetail detail = GeofencingSubscriptionDetail.builder()
                .device(deviceInfo)
                .area(area)
                .subscriptionMaxEvents(10)
                .build();

        GeofencingSubscriptionConfig config = GeofencingSubscriptionConfig.builder()
                .subscriptionDetail(detail)
                .build();

        return GeofencingSubscription.builder()
                .subscriptionId("mock-subscription-" + System.currentTimeMillis())
                .protocol(request != null ? request.getProtocol() : "HTTP")
                .sink(request != null ? request.getSink() : "https://example.com/notifications")
                .types(request != null ? request.getTypes() :
                       java.util.Collections.singletonList("org.camaraproject.geofencing-subscriptions.v0.area-entered"))
                .config(config)
                .startsAt(java.time.Instant.now().toString())
                .expiresAt(java.time.Instant.now().plusSeconds(3600).toString())
                .build();
    }
}

