package com.talenArena.SafeZone.controller;

import com.talenArena.SafeZone.models.CreateSessionRequest;
import com.talenArena.SafeZone.models.QodSession;
import com.talenArena.SafeZone.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/qod")
@RequiredArgsConstructor
public class NetworkAsCodeController {

    private final DeviceService deviceService;

    /**
     * Crea una sesión de Calidad de Servicio (QoD).
     * @param request Los datos para crear la sesión.
     * @return La sesión QoD creada.
     */
    @PostMapping("/sessions")
    public ResponseEntity<QodSession> createQodSession(@RequestBody CreateSessionRequest request) {
        log.info("Recibida petición para crear sesión QoD con perfil: {}", request.getProfile());
        QodSession session = deviceService.createQodSession(request);
        log.info("Sesión QoD creada exitosamente. ID: {}", session.getSessionId());
        return ResponseEntity.ok(session);
    }

    /**
     * Obtiene una sesión QoD existente por su ID.
     * @param sessionId El ID de la sesión.
     * @return Los detalles de la sesión.
     */
    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<QodSession> getQodSession(@PathVariable String sessionId) {
        log.info("Obteniendo sesión QoD: {}", sessionId);
        QodSession session = deviceService.getQodSession(sessionId);
        return ResponseEntity.ok(session);
    }

    /**
     * Elimina una sesión QoD existente.
     * @param sessionId El ID de la sesión a eliminar.
     * @return Una respuesta sin contenido.
     */
    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<Void> deleteQodSession(@PathVariable String sessionId) {
        log.info("Eliminando sesión QoD: {}", sessionId);
        deviceService.deleteQodSession(sessionId);
        return ResponseEntity.noContent().build();
    }
}
