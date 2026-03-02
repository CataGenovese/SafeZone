package com.talenArena.SafeZone.controller;

import com.talenArena.SafeZone.dto.Auth.AuthRequestDto;
import com.talenArena.SafeZone.dto.Auth.AuthResponseDto;
import com.talenArena.SafeZone.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Autenticación", description = "Endpoints de autenticación con validaciones Nokia")
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint de autenticación con verificaciones Nokia
     *
     * @param request Solicitud con phoneNumber
     * @return AuthResponseDto con resultados de Location, KYC y SimSwap
     */
    @PostMapping("/authentication")
    @Operation(
        summary = "Autenticación de usuario",
        description = "Autentica un usuario mediante número de teléfono y realiza validaciones de ubicación, KYC y SIM swap"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Autenticación exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    public ResponseEntity<AuthResponseDto> authentication(@Valid @RequestBody AuthRequestDto request) {
        try {
            log.info("Procesando autenticación para phoneNumber: {}", request.getPhoneNumber());
            AuthResponseDto response = authService.authenticate(request);
            log.info("Autenticación completada para phoneNumber: {}", request.getPhoneNumber());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.warn("Fallo de autenticación para phoneNumber: {} - Error: {}",
                request.getPhoneNumber(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(AuthResponseDto.builder().build());
        } catch (Exception e) {
            log.error("Error inesperado durante autenticación: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AuthResponseDto.builder().build());
        }
    }
}
