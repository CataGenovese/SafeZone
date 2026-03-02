package com.talenArena.SafeZone.service;

import com.talenArena.SafeZone.dto.SimSwap.SimSwapApiRequest;
import com.talenArena.SafeZone.dto.SimSwap.SimSwapResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimSwapService {

    private final WebClient networkAsCodeWebClient;

    @Value("${network.as.code.mock.enabled:false}")
    private boolean mockEnabled;

    /**
     * Verifica si ha ocurrido un cambio de SIM asociado a un número de teléfono.
     * Esta capacidad permite comprobar si una tarjeta SIM ha sido cambiada recientemente,
     * lo cual es útil para prevenir fraudes en autenticación (MFA, transacciones bancarias).
     *
     * @param phoneNumber El número de teléfono a verificar (formato E.164, ej: +34123456789)
     * @param maxAge (Opcional) Tiempo en horas hacia atrás para verificar el cambio.
     *               Si se deja null, se usa el valor por defecto de la API.
     * @return SimSwapResponse con el resultado de la verificación
     */
    public SimSwapResponse checkSimSwap(String phoneNumber, Integer maxAge) {
        log.info("Verificando SIM Swap para el número: {}", phoneNumber);

        if (mockEnabled) {
            log.warn("MODO SIMULACIÓN: Retornando respuesta mock para SIM Swap.");
            SimSwapResponse response = new SimSwapResponse();
            // Simulamos que NO hubo cambio de SIM (swapped = false) para el caso feliz
            response.setSwapped(false);
            return response;
        }

        // Construir el request body adecuado
        SimSwapApiRequest requestBody = new SimSwapApiRequest(phoneNumber);
        if (maxAge != null) {
            requestBody.setMaxAge(maxAge);
        }

        try {
            // endpoint relativo de la API SIM Swap
            String uri = "/sim-swap/v0/check";

            log.info("Realizando petición POST a: {}", uri);
            log.debug("Request body: {}", requestBody);

            return networkAsCodeWebClient.post()
                    .uri(uri)
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .doOnNext(errorBody -> log.error("Error de la API - Status: {}, Body: {}",
                                            clientResponse.statusCode(), errorBody))
                                    .flatMap(errorBody -> {
                                        String errorMsg = String.format("API Error [%s]: %s",
                                                clientResponse.statusCode(), errorBody);
                                        return reactor.core.publisher.Mono.error(new RuntimeException(errorMsg));
                                    })
                    )
                    .bodyToMono(SimSwapResponse.class)
                    .block();

        } catch (Exception e) {
            log.error("Error al verificar SIM Swap: {}", e.getMessage(), e);
            throw new RuntimeException("Error en la verificación de SIM Swap", e);
        }
    }

    // Se eliminaron las clases internas estáticas
}
