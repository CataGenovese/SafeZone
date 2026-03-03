package com.talenArena.SafeZone.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talenArena.SafeZone.dto.SafeZoneRequest;
import com.talenArena.SafeZone.dto.DataMatchResult;
import com.talenArena.SafeZone.entities.Cliente;
import com.talenArena.SafeZone.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ObjectMapper objectMapper;

    public Cliente actualizarDatosCliente(Long clienteId, SafeZoneRequest request) {
        log.info("Actualizando datos del cliente {} con teléfono {}", clienteId, request.getPhoneNumber());

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + clienteId));

        try {
            String datosJson = objectMapper.writeValueAsString(request);
            cliente.setDatos(datosJson);

            Cliente updated = clienteRepository.save(cliente);
            log.info("Cliente {} actualizado correctamente", updated.getId());
            return updated;
        } catch (Exception e) {
            log.error("Error al actualizar datos del cliente: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar datos del cliente", e);
        }
    }

    public DataMatchResult verificarDatosCliente(Long clienteId, SafeZoneRequest request) {
        log.info("Verificando datos del cliente {} contra BBDD", clienteId);

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + clienteId));

        try {
            String requestJson = objectMapper.writeValueAsString(request);
            String storedJson = cliente.getDatos();

            SafeZoneRequest storedRequest = objectMapper.readValue(storedJson, SafeZoneRequest.class);
            String storedNormalized = objectMapper.writeValueAsString(storedRequest);

            boolean match = requestJson.equals(storedNormalized);

            log.info("Verificación cliente {}: datos {} coinciden", clienteId, match ? "SÍ" : "NO");

            return DataMatchResult.builder()
                    .executed(true)
                    .match(match)
                    .storedData(storedJson)
                    .requestData(requestJson)
                    .build();
        } catch (Exception e) {
            log.error("Error al verificar datos del cliente: {}", e.getMessage(), e);
            return DataMatchResult.builder()
                    .executed(false)
                    .match(false)
                    .error(e.getMessage())
                    .build();
        }
    }
}
