package com.talenArena.SafeZone.controller;

import com.talenArena.SafeZone.dto.*;
import com.talenArena.SafeZone.entities.Cliente;
import com.talenArena.SafeZone.service.ClienteService;
import com.talenArena.SafeZone.service.KYCService;
import com.talenArena.SafeZone.service.LocationVerificationService;
import com.talenArena.SafeZone.service.SignInService;
import com.talenArena.SafeZone.service.SimSwapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/safezone")
@RequiredArgsConstructor
public class SafeZoneController {

    private final SimSwapService simSwapService;
    private final LocationVerificationService locationVerificationService;
    private final KYCService kycService;
    private final SignInService signInService;
    private final ClienteService clienteService;

    @PutMapping("/update-client/{clienteId}")
    @Operation(summary = "Actualizar datos de cliente",
            description = "Actualiza el campo 'datos' (JSON) de un cliente existente con la información del SafeZoneRequest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public ResponseEntity<ClienteSaveResponse> updateClient(
            @PathVariable Long clienteId,
            @RequestBody SafeZoneRequest request) {
        log.info("Actualizando datos del cliente {}", clienteId);
        try {
            Cliente cliente = clienteService.actualizarDatosCliente(clienteId, request);
            ClienteSaveResponse response = ClienteSaveResponse.builder()
                    .id(cliente.getId())
                    .empresaId(cliente.getEmpresa().getId())
                    .datos(cliente.getDatos())
                    .message("Cliente actualizado correctamente")
                    .build();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error al actualizar cliente: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                    ClienteSaveResponse.builder()
                            .message("Error: " + e.getMessage())
                            .build()
            );
        }
    }

    @PostMapping("/full-check")
    @Operation(summary = "Verificación completa SafeZone",
            description = "Verifica datos contra BBDD y ejecuta: SIM Swap, Location Verification y KYC")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación completada"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public ResponseEntity<SafeZoneResponse> fullCheck(@RequestBody SafeZoneRequest request) {
        log.info("Iniciando verificación completa SafeZone para: {}", request.getPhoneNumber());

        // 1. Verificar datos contra BBDD
        DataMatchResult dataMatchResult = executeDataMatch(request);

        // Si los datos no coinciden, no ejecutar las APIs externas
        if (dataMatchResult.isExecuted() && !dataMatchResult.isMatch()) {
            log.warn("Los datos no coinciden con la BBDD. Abortando verificaciones externas.");
            SafeZoneResponse response = SafeZoneResponse.builder()
                    .success(false)
                    .message("Los datos enviados no coinciden con los almacenados en la BBDD")
                    .dataMatch(dataMatchResult)
                    .build();
            return ResponseEntity.ok(response);
        }

        // 2. Ejecutar verificaciones externas
        SimSwapResult simSwapResult = executeSimSwap(request);
        LocationResult locationResult = executeLocationVerification(request);
        KycResult kycResult = executeKyc(request);

        boolean allSuccess = dataMatchResult.getError() == null
                && simSwapResult.getError() == null
                && locationResult.getError() == null
                && kycResult.getError() == null;

        SafeZoneResponse response = SafeZoneResponse.builder()
                .success(allSuccess)
                .message(allSuccess ? "Todas las verificaciones completadas" : "Algunas verificaciones fallaron")
                .dataMatch(dataMatchResult)
                .simSwap(simSwapResult)
                .location(locationResult)
                .kyc(kycResult)
                .build();

        log.info("Verificación completa finalizada. Éxito: {}", allSuccess);
        return ResponseEntity.ok(response);
    }

    // ===============================
    // VERIFICACIÓN DATOS CONTRA BBDD
    // ===============================
    private DataMatchResult executeDataMatch(SafeZoneRequest request) {
        if (request.getClienteId() == null) {
            return DataMatchResult.builder()
                    .executed(false)
                    .match(false)
                    .error("clienteId es requerido para la verificación contra BBDD")
                    .build();
        }

        try {
            log.info(">> Verificando datos contra BBDD para cliente: {}", request.getClienteId());
            return clienteService.verificarDatosCliente(request.getClienteId(), request);
        } catch (Exception e) {
            log.error("Error en verificación contra BBDD: {}", e.getMessage());
            return DataMatchResult.builder()
                    .executed(false)
                    .match(false)
                    .error(e.getMessage())
                    .build();
        }
    }

    // ===============================
    // SIM SWAP
    // ===============================
    private SimSwapResult executeSimSwap(SafeZoneRequest request) {
        try {
            log.info(">> Ejecutando SIM Swap check para: {}", request.getPhoneNumber());
            SimSwapResponse simSwapResponse = simSwapService.checkSimSwap(
                    request.getPhoneNumber(),
                    request.getMaxAge() != null ? request.getMaxAge() : 120
            );
            return SimSwapResult.builder()
                    .executed(true)
                    .swapped(simSwapResponse.isSwapped())
                    .build();
        } catch (Exception e) {
            log.error("Error en SIM Swap: {}", e.getMessage());
            return SimSwapResult.builder()
                    .executed(false)
                    .error(e.getMessage())
                    .build();
        }
    }

    // ===============================
    // LOCATION VERIFICATION
    // ===============================
    private LocationResult executeLocationVerification(SafeZoneRequest request) {
        if (request.getLatitude() == null || request.getLongitude() == null) {
            return LocationResult.builder()
                    .executed(false)
                    .error("Latitud y longitud son requeridas para la verificación de ubicación")
                    .build();
        }

        try {
            log.info(">> Ejecutando Location Verification para: {}", request.getPhoneNumber());

            Device device = new Device();
            device.setPhoneNumber(request.getPhoneNumber());

            Center center = new Center();
            center.setLatitude(request.getLatitude());
            center.setLongitude(request.getLongitude());

            Area area = new Area();
            area.setAreaType("CIRCLE");
            area.setCenter(center);
            area.setRadius(request.getRadius() != null ? request.getRadius() : 50000);

            LocationVerificationRequest locationRequest = new LocationVerificationRequest();
            locationRequest.setDevice(device);
            locationRequest.setArea(area);

            String result = locationVerificationService.verifyLocation(locationRequest);
            return LocationResult.builder()
                    .executed(true)
                    .verificationResult(result)
                    .build();
        } catch (Exception e) {
            log.error("Error en Location Verification: {}", e.getMessage());
            return LocationResult.builder()
                    .executed(false)
                    .error(e.getMessage())
                    .build();
        }
    }

    // ===============================
    // KYC (Know Your Customer)
    // ===============================
    private KycResult executeKyc(SafeZoneRequest request) {
        try {
            log.info(">> Ejecutando KYC para: {}", request.getPhoneNumber());

            PersonInfoRequest personInfoRequest = new PersonInfoRequest();
            personInfoRequest.setPhoneNumber(request.getPhoneNumber());

            PersonInfoResponse personInfo = kycService.getPersonInfo(personInfoRequest);
            return KycResult.builder()
                    .executed(true)
                    .personInfo(personInfo)
                    .build();
        } catch (Exception e) {
            log.error("Error en KYC: {}", e.getMessage());
            return KycResult.builder()
                    .executed(false)
                    .error(e.getMessage())
                    .build();
        }
    }

    // ===============================
    // SCORE CHECK
    // ===============================
    @PostMapping("/score-check")
    @Operation(summary = "Verificación con score",
            description = "Ejecuta el full-check y devuelve el score (número de verificaciones que pasaron)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Score de la verificación"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public ResponseEntity<SafeZoneScoreResponse> scoreCheck(@RequestBody SafeZoneRequest request) {
        log.info("Iniciando score-check para: {}", request.getPhoneNumber());

        int totalChecks = 0;
        int passed = 0;

        // 1. Data Match contra BBDD
        DataMatchResult dataMatchResult = executeDataMatch(request);
        if (dataMatchResult.isExecuted()) {
            totalChecks++;
            if (dataMatchResult.isMatch() && dataMatchResult.getError() == null) {
                passed++;
            }
        }

        // 2. SIM Swap
        SimSwapResult simSwapResult = executeSimSwap(request);
        if (simSwapResult.isExecuted()) {
            totalChecks++;
            if (!simSwapResult.isSwapped() && simSwapResult.getError() == null) {
                passed++;
            }
        }

        // 3. Location Verification
        LocationResult locationResult = executeLocationVerification(request);
        if (locationResult.isExecuted()) {
            totalChecks++;
            if (locationResult.getError() == null
                    && "TRUE".equalsIgnoreCase(locationResult.getVerificationResult())) {
                passed++;
            }
        }

        // 4. KYC
        KycResult kycResult = executeKyc(request);
        if (kycResult.isExecuted()) {
            totalChecks++;
            if (kycResult.getPersonInfo() != null && kycResult.getError() == null) {
                passed++;
            }
        }

        // Calcular score de 1 a 100
        int score = totalChecks > 0 ? (int) Math.round((double) passed / totalChecks * 100) : 0;
        boolean approved = totalChecks > 0 && passed == totalChecks;

        log.info("Score-check finalizado. Passed: {}/{}, Score: {}, Approved: {}", passed, totalChecks, score, approved);
        return ResponseEntity.ok(SafeZoneScoreResponse.builder()
                .score(score)
                .approved(approved)
                .build());
    }


}

