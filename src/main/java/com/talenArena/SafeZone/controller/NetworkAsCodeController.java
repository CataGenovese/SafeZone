package com.talenArena.SafeZone.controller;

import com.talenArena.SafeZone.models.Device;
import com.talenArena.SafeZone.models.DeviceIpv4Addr;
import com.talenArena.SafeZone.models.QodSession;
import com.talenArena.SafeZone.service.DeviceService;
import com.talenArena.SafeZone.service.NetworkAsCodeClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/network")
@RequiredArgsConstructor
public class NetworkAsCodeController {

    private final NetworkAsCodeClient networkAsCodeClient;
    private final DeviceService deviceService;

    /**
     * Ejemplo de endpoint para crear una sesión QoD
     */
    @PostMapping("/qod-session")
    public ResponseEntity<QodSession> createQodSession(@RequestBody CreateQodSessionDto request) {
        log.info("Recibida petición para crear sesión QoD");

        // Crear el objeto Device
        DeviceIpv4Addr ipv4Addr = DeviceIpv4Addr.builder()
                .publicAddress(request.getPublicAddress())
                .privateAddress(request.getPrivateAddress())
                .publicPort(request.getPublicPort())
                .build();

        Device device = networkAsCodeClient.getDevice(
                ipv4Addr,
                request.getIpv6Address(),
                request.getPhoneNumber()
        );

        // Crear la sesión QoD
        QodSession session = deviceService.createQodSession(
                device,
                request.getServiceIpv4(),
                request.getServiceIpv6(),
                request.getProfile(),
                request.getDuration()
        );

        return ResponseEntity.ok(session);
    }

    /**
     * Obtiene una sesión QoD existente
     */
    @GetMapping("/qod-session/{sessionId}")
    public ResponseEntity<QodSession> getQodSession(@PathVariable String sessionId) {
        log.info("Obteniendo sesión QoD: {}", sessionId);
        QodSession session = deviceService.getQodSession(sessionId);
        return ResponseEntity.ok(session);
    }

    /**
     * Elimina una sesión QoD
     */
    @DeleteMapping("/qod-session/{sessionId}")
    public ResponseEntity<Void> deleteQodSession(@PathVariable String sessionId) {
        log.info("Eliminando sesión QoD: {}", sessionId);
        deviceService.deleteQodSession(sessionId);
        return ResponseEntity.noContent().build();
    }

    // DTO para la petición
    @lombok.Data
    public static class CreateQodSessionDto {
        private String publicAddress;
        private String privateAddress;
        private Integer publicPort;
        private String ipv6Address;
        private String phoneNumber;
        private String serviceIpv4;
        private String serviceIpv6;
        private String profile;
        private Integer duration;
    }
}

