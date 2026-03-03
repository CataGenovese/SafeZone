package com.talenArena.SafeZone.controller;

import com.talenArena.SafeZone.dto.*;
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

    @PostMapping("/full-check")
    @Operation(summary = "Verificación completa SafeZone",
            description = "Ejecuta todas las verificaciones: SIM Swap, Location Verification, KYC y Registro de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación completada"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public ResponseEntity<SafeZoneResponse> fullCheck(@RequestBody SafeZoneRequest request) {
        log.info("Iniciando verificación completa SafeZone para: {}", request.getPhoneNumber());

        SimSwapResult simSwapResult = executeSimSwap(request);
        LocationResult locationResult = executeLocationVerification(request);
        KycResult kycResult = executeKyc(request);

        boolean allSuccess = simSwapResult.getError() == null
                || locationResult.getError() == null
                || kycResult.getError() == null;

        SafeZoneResponse response = SafeZoneResponse.builder()
                .success(allSuccess)
                .message(allSuccess ? "Todas las verificaciones completadas" : "Algunas verificaciones fallaron")
                .simSwap(simSwapResult)
                .location(locationResult)
                .kyc(kycResult)
                .build();

        log.info("Verificación completa finalizada. Éxito: {}", allSuccess);
        return ResponseEntity.ok(response);
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


}

