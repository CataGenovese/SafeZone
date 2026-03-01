package com.talenArena.SafeZone.controller;

import com.talenArena.SafeZone.models.geofencing.CreateGeofencingSubscriptionRequest;
import com.talenArena.SafeZone.models.geofencing.GeofencingSubscription;
import com.talenArena.SafeZone.service.GeofencingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/geofencing")
@RequiredArgsConstructor
public class GeofencingController {

    private final GeofencingService geofencingService;

    @PostMapping("/subscriptions")
    public Mono<ResponseEntity<GeofencingSubscription>> createSubscription(
            @RequestBody CreateGeofencingSubscriptionRequest request) {

        log.info("Recibida solicitud para crear suscripcion de geofencing: {}", request);

        return geofencingService.createSubscription(request)
                .map(subscription -> ResponseEntity.status(HttpStatus.CREATED).body(subscription))
                .onErrorResume(error -> {
                    log.error("Error al crear suscripcion", error);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/subscriptions/{subscriptionId}")
    public Mono<ResponseEntity<GeofencingSubscription>> getSubscription(
            @PathVariable String subscriptionId) {

        log.info("Obteniendo suscripcion: {}", subscriptionId);

        return geofencingService.getSubscription(subscriptionId)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> {
                    log.error("Error al obtener suscripcion", error);
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
                });
    }

    @DeleteMapping("/subscriptions/{subscriptionId}")
    public Mono<ResponseEntity<Void>> deleteSubscription(
            @PathVariable String subscriptionId) {

        log.info("Eliminando suscripcion: {}", subscriptionId);

        return geofencingService.deleteSubscription(subscriptionId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorResume(error -> {
                    log.error("Error al eliminar suscripcion", error);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/subscriptions")
    public Mono<ResponseEntity<GeofencingSubscription[]>> listSubscriptions() {
        log.info("Listando todas las suscripciones");

        return geofencingService.listSubscriptions()
                .map(ResponseEntity::ok)
                .onErrorResume(error -> {
                    log.error("Error al listar suscripciones", error);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }
}

