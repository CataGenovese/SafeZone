package com.talenArena.SafeZone.service;

import com.talenArena.SafeZone.models.Device;
import com.talenArena.SafeZone.models.QodSession;
import com.talenArena.SafeZone.models.CreateQodSessionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceService {

    private final WebClient networkAsCodeWebClient;

    /**
     * Crea una sesión de Quality on Demand (QoD) para un dispositivo
     *
     * @param device Dispositivo para el cual crear la sesión
     * @param serviceIpv4 Dirección IPv4 del servicio
     * @param serviceIpv6 Dirección IPv6 del servicio
     * @param profile Perfil de QoS (ej: "DOWNLINK_L_UPLINK_L")
     * @param duration Duración de la sesión en segundos
     * @return QodSession objeto que representa la sesión creada
     */
    public QodSession createQodSession(Device device, String serviceIpv4, String serviceIpv6,
                                       String profile, Integer duration) {
        log.info("Creando sesión QoD para dispositivo con perfil: {} y duración: {} segundos",
                profile, duration);

        CreateQodSessionRequest request = CreateQodSessionRequest.builder()
                .device(device)
                .serviceIpv4(serviceIpv4)
                .serviceIpv6(serviceIpv6)
                .qosProfile(profile)
                .duration(duration)
                .build();

        try {
            QodSession session = networkAsCodeWebClient.post()
                    .uri("/qod/v0/sessions")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(QodSession.class)
                    .block();

            log.info("Sesión QoD creada exitosamente con ID: {}", session.getSessionId());
            return session;

        } catch (Exception e) {
            log.error("Error al crear sesión QoD: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo crear la sesión QoD", e);
        }
    }

    /**
     * Obtiene una sesión QoD existente
     *
     * @param sessionId ID de la sesión
     * @return QodSession objeto que representa la sesión
     */
    public QodSession getQodSession(String sessionId) {
        log.info("Obteniendo sesión QoD con ID: {}", sessionId);

        try {
            QodSession session = networkAsCodeWebClient.get()
                    .uri("/qod/v0/sessions/{sessionId}", sessionId)
                    .retrieve()
                    .bodyToMono(QodSession.class)
                    .block();

            log.info("Sesión QoD obtenida exitosamente");
            return session;

        } catch (Exception e) {
            log.error("Error al obtener sesión QoD: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo obtener la sesión QoD", e);
        }
    }

    /**
     * Elimina una sesión QoD
     *
     * @param sessionId ID de la sesión a eliminar
     */
    public void deleteQodSession(String sessionId) {
        log.info("Eliminando sesión QoD con ID: {}", sessionId);

        try {
            networkAsCodeWebClient.delete()
                    .uri("/qod/v0/sessions/{sessionId}", sessionId)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

            log.info("Sesión QoD eliminada exitosamente");

        } catch (Exception e) {
            log.error("Error al eliminar sesión QoD: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo eliminar la sesión QoD", e);
        }
    }
}

