package com.talenArena.SafeZone.controller;

import com.talenArena.SafeZone.models.geofencing.GeofencingNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/webhooks/geofencing")
@RequiredArgsConstructor
public class GeofencingWebhookController {

    @PostMapping("/notifications")
    public ResponseEntity<Void> receiveNotification(@RequestBody GeofencingNotification notification) {

        log.info("Notificacion de geofencing recibida:");
        log.info("  - Tipo: {}", notification.getType());
        log.info("  - Subscription ID: {}", notification.getSubscriptionId());
        log.info("  - Tiempo del evento: {}", notification.getEventTime());
        log.info("  - Dispositivo: {}", notification.getDevice());
        log.info("  - Area: {}", notification.getArea());

        return ResponseEntity.ok().build();
    }
}
