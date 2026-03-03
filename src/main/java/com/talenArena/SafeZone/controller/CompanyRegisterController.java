package com.talenArena.SafeZone.controller;

import com.talenArena.SafeZone.dto.CompanySignInRequestDto;
import com.talenArena.SafeZone.dto.CompanySignInResponseDto;
import com.talenArena.SafeZone.service.CompanyRegisterService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class CompanyRegisterController {

    private final CompanyRegisterService companyRegisterService;

    @PostMapping("/register/company")
    @Operation(summary = "Registro de nueva empresa", description = "Registra una nueva empresa y crea un usuario administrador")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Registro exitoso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CompanySignInResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos o empresa/email ya registrado"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    public ResponseEntity<CompanySignInResponseDto> register(@Valid @RequestBody CompanySignInRequestDto request) {
        try {
            log.info("Procesando registro para empresa: {}", request.getName());
            CompanySignInResponseDto response = companyRegisterService.signIn(request);
            log.info("Registro completado exitosamente para empresa: {} y usuario: {}", request.getName(), request.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            log.warn("Fallo en registro para empresa: {} - Error: {}", request.getName(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CompanySignInResponseDto.builder()
                            .build());
        } catch (Exception e) {
            log.error("Error inesperado durante registro: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CompanySignInResponseDto.builder()
                            .build());
        }
    }
}



