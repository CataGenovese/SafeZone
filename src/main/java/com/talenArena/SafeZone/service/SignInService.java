package com.talenArena.SafeZone.service;

import com.talenArena.SafeZone.dto.SignIn.SignInRequestDto;
import com.talenArena.SafeZone.dto.SignIn.SignInResponseDto;
import com.talenArena.SafeZone.entities.Empleado;
import com.talenArena.SafeZone.entities.Empresa;
import com.talenArena.SafeZone.repository.EmpleadoRepository;
import com.talenArena.SafeZone.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignInService {

    private final EmpleadoRepository empleadoRepository;
    private final EmpresaRepository empresaRepository;

    public SignInResponseDto signIn(SignInRequestDto request) {
        log.info("Intento de registro para usuario: {}", request.getEmail());

        Optional<Empleado> empleadoExistente = empleadoRepository.findByEmail(request.getEmail());
        if (empleadoExistente.isPresent()) {
            log.warn("Intento de registro con email existente: {}", request.getEmail());
            throw new RuntimeException("El email ya está registrado");
        }

        Empresa empresa = empresaRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay empresas disponibles"));

        Empleado nuevoEmpleado = Empleado.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .nombre("Usuario")
                .apellido("Registrado")
                .rol("EMPLOYEE")
                .activo(true)
                .empresa(empresa)
                .build();

        Empleado empleadoGuardado = empleadoRepository.save(nuevoEmpleado);

        log.info("Registro exitoso para usuario: {}", request.getEmail());

        return SignInResponseDto.builder()
                .email(empleadoGuardado.getEmail())
                .password(empleadoGuardado.getPassword())
                .build();
    }
}

