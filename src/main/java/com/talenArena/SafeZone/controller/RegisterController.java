package com.talenArena.SafeZone.controller;

import com.talenArena.SafeZone.dto.SignInRequestDto;
import com.talenArena.SafeZone.dto.SignInResponseDto;
import com.talenArena.SafeZone.service.SignInService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
public class RegisterController {

    private final SignInService signInService;

    @PostMapping("/register")
    @Operation(summary = "Registro de nuevo usuario", description = "Registra un nuevo usuario en el sistema")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Registro exitoso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SignInResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos o email ya registrado"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    public ResponseEntity<Void> register(@Valid @RequestBody SignInRequestDto request) {
        try {
            log.info("Procesando registro para: {}", request.getEmail());
            signInService.signIn(request);
            log.info("Registro completado exitosamente para: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            log.warn("Fallo en registro para: {} - Error: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Error inesperado durante registro: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}



