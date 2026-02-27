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
     * Crea una sesión QoD (Quality on Demand)
     *
     * IMPORTANTE: El ancho de banda y la prioridad se asignan AUTOMÁTICAMENTE
     * según el perfil QoS que selecciones. NO necesitas especificar valores manualmente.
     *
     * ¿Cómo funciona?
     * 1. Seleccionas un perfil QoS (ej: "DOWNLINK_L_UPLINK_L")
     * 2. La red móvil asigna automáticamente:
     *    - Ancho de banda garantizado
     *    - Latencia máxima
     *    - Prioridad sobre usuarios normales
     * 3. Tu dispositivo obtiene estos recursos durante la duración especificada
     *
     * Perfiles disponibles:
     * - DOWNLINK_L_UPLINK_L: Baja latencia (gaming, videollamadas)
     * - DOWNLINK_M_UPLINK_M: Latencia media (streaming, navegación)
     * - DOWNLINK_H_UPLINK_H: Alto rendimiento (transferencias grandes)
     * - DOWNLINK_XL_UPLINK_XL: Rendimiento extra alto (backups, CDN)
     *
     * @param request Datos para crear la sesión QoD
     * @return Sesión QoD creada con recursos asignados automáticamente
     */
    @PostMapping("/qod-session")
    public ResponseEntity<QodSession> createQodSession(@RequestBody CreateQodSessionDto request) {
        log.info("Recibida petición para crear sesión QoD con perfil: {}", request.getProfile());

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
        // La red asignará automáticamente ancho de banda y prioridad según el perfil
        QodSession session = deviceService.createQodSession(
                device,
                request.getServiceIpv4(),
                request.getServiceIpv6(),
                request.getProfile(),
                request.getDuration()
        );

        log.info("Sesión QoD creada exitosamente. ID: {}, Estado: {}",
                session.getSessionId(), session.getStatus());

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

    /**
     * Lista los perfiles QoS disponibles
     *
     * Este endpoint te muestra todos los perfiles QoS que puedes usar.
     * Cada perfil tiene configuraciones predefinidas de ancho de banda y latencia.
     *
     * @return Lista de perfiles QoS disponibles con sus descripciones
     */
    @GetMapping("/qos-profiles")
    public ResponseEntity<?> getAvailableQosProfiles() {
        log.info("Consultando perfiles QoS disponibles");

        var profiles = java.util.Arrays.stream(com.talenArena.SafeZone.models.QosProfile.values())
                .map(profile -> new QosProfileInfo(
                        profile.getValue(),
                        profile.getDescription()
                ))
                .toList();

        return ResponseEntity.ok(profiles);
    }

    // DTO para la petición de creación de sesión
    @lombok.Data
    public static class CreateQodSessionDto {
        /**
         * Dirección IP pública del dispositivo
         */
        private String publicAddress;

        /**
         * Dirección IP privada del dispositivo (opcional)
         */
        private String privateAddress;

        /**
         * Puerto público del dispositivo (opcional)
         */
        private Integer publicPort;

        /**
         * Dirección IPv6 del dispositivo (opcional)
         */
        private String ipv6Address;

        /**
         * Número de teléfono del dispositivo (opcional)
         */
        private String phoneNumber;

        /**
         * Dirección IPv4 del servicio/aplicación
         */
        private String serviceIpv4;

        /**
         * Dirección IPv6 del servicio/aplicación (opcional)
         */
        private String serviceIpv6;

        /**
         * Perfil QoS a aplicar (OBLIGATORIO)
         * Valores válidos:
         * - DOWNLINK_L_UPLINK_L
         * - DOWNLINK_M_UPLINK_M
         * - DOWNLINK_H_UPLINK_H
         * - DOWNLINK_XL_UPLINK_XL
         *
         * Este perfil determina automáticamente el ancho de banda y prioridad
         */
        private String profile;

        /**
         * Duración de la sesión en segundos
         * Ejemplo: 3600 = 1 hora
         */
        private Integer duration;
    }

    // DTO para información de perfil QoS
    @lombok.Data
    @lombok.AllArgsConstructor
    public static class QosProfileInfo {
        private String value;
        private String description;
    }
}

