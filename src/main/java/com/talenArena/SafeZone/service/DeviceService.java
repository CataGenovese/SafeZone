package com.talenArena.SafeZone.service;

import com.talenArena.SafeZone.models.QoS.CreateSessionRequest;
import com.talenArena.SafeZone.models.QoS.Device;
import com.talenArena.SafeZone.models.QoS.DeviceIpv4Addr;
import com.talenArena.SafeZone.models.QoS.QodSession;
import com.talenArena.SafeZone.models.QoS.CreateQodSessionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceService {

    private final WebClient networkAsCodeWebClient;

    @Value("${network.as.code.mock.enabled:false}")
    private boolean mockEnabled;

    public QodSession createQodSession(CreateSessionRequest qodRequest) {
        log.info("Creando sesión QoD para dispositivo con perfil: {} y duración: {} segundos",
                qodRequest.getProfile(), qodRequest.getDuration());

        if (mockEnabled) {
            log.warn("MODO SIMULACIÓN: La llamada a la API externa no se realizará. Devolviendo una respuesta simulada.");
            return createMockSession(qodRequest);
        }

        DeviceIpv4Addr ipv4Addr = DeviceIpv4Addr.builder()
                .publicAddress(qodRequest.getPublicAddress())
                .build();

        Device device = Device.builder()
                .ipv4Address(ipv4Addr)
                .build();

        CreateQodSessionRequest request = CreateQodSessionRequest.builder()
                .device(device)
                .serviceIpv4(qodRequest.getServiceIpv4())
                .qosProfile(qodRequest.getProfile())
                .duration(qodRequest.getDuration())
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

    private QodSession createMockSession(CreateSessionRequest qodRequest) {
        QodSession mockSession = new QodSession();
        mockSession.setSessionId(UUID.randomUUID().toString());
        mockSession.setQosProfile(qodRequest.getProfile());
        mockSession.setDuration(qodRequest.getDuration());
        mockSession.setStartedAt(LocalDateTime.now());
        mockSession.setExpiresAt(LocalDateTime.now().plusSeconds(qodRequest.getDuration()));
        return mockSession;
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
