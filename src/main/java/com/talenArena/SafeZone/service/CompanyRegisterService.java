package com.talenArena.SafeZone.service;

import com.talenArena.SafeZone.dto.ApiType;
import com.talenArena.SafeZone.dto.CompanySignInRequestDto;
import com.talenArena.SafeZone.dto.CompanySignInResponseDto;
import com.talenArena.SafeZone.entities.Api;
import com.talenArena.SafeZone.entities.Empleado;
import com.talenArena.SafeZone.entities.Empresa;
import com.talenArena.SafeZone.entities.EmpresaApi;
import com.talenArena.SafeZone.repository.ApiRepository;
import com.talenArena.SafeZone.repository.EmpleadoRepository;
import com.talenArena.SafeZone.repository.EmpresaApiRepository;
import com.talenArena.SafeZone.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyRegisterService {

    private final EmpleadoRepository empleadoRepository;
    private final EmpresaRepository empresaRepository;
    private final ApiRepository apiRepository;
    private final EmpresaApiRepository empresaApiRepository;

    @Transactional
    public CompanySignInResponseDto signIn(CompanySignInRequestDto request) {
        log.info("Intento de registro para empresa: {}", request.getName());

        Optional<Empresa> empresaExistente = empresaRepository.findByName(request.getName());
        if (empresaExistente.isPresent()) {
            log.warn("Intento de registro con nombre de empresa existente: {}", request.getName());
            throw new RuntimeException("La empresa ya está registrada");
        }

        Optional<Empleado> empleadoExistente = empleadoRepository.findByEmail(request.getEmail());
        if (empleadoExistente.isPresent()) {
            log.warn("Intento de registro con email existente: {}", request.getEmail());
            throw new RuntimeException("El email ya está registrado");
        }

        Empresa nuevaEmpresa = Empresa.builder()
                .name(request.getName())
                .build();

        Empresa empresaGuardada = empresaRepository.save(nuevaEmpresa);


        Empleado nuevoEmpleado = Empleado.builder()
                .email(request.getEmail())
                .nombre("Administrador")
                .apellido(request.getName())
                .rol("ADMIN")
                .activo(true)
                .empresa(empresaGuardada)
                .build();


        if (request.getApiType() != null && !request.getApiType().isEmpty()) {
            for (ApiType apiType : request.getApiType()) {
                switch (apiType) {
                    case LOCATION_VERIFICATION:
                        asociarApi(empresaGuardada, "LOCATION_VERIFICATION",
                                "Verificación de ubicación del dispositivo",
                                "/location-verification/v0/verify");
                        break;
                    case SIM_SWAP_DETECTION:
                        asociarApi(empresaGuardada, "SIM_SWAP_DETECTION",
                                "Detección de cambio de tarjeta SIM",
                                "/sim-swap/v0/check");
                        break;
                    case KYC_VERIFICATION:
                        asociarApi(empresaGuardada, "KYC_VERIFICATION",
                                "Verificación de identidad KYC",
                                "/kyc-match/v0/match");
                        break;
                }
            }
        }

        log.info("Registro exitoso para empresa: {} y usuario: {}", request.getName(), request.getEmail());

        return CompanySignInResponseDto.builder()
                .companyName(empresaGuardada.getName())
                .apiType(request.getApiType())
                .build();
    }


    private void asociarApi(Empresa empresa, String nombre, String descripcion, String endpoint) {
        Optional<Api> apiOptional = apiRepository.findByNombre(nombre);
        Api api;

        if (apiOptional.isPresent()) {
            api = apiOptional.get();
        } else {
            api = Api.builder()
                    .nombre(nombre)
                    .descripcion(descripcion)
                    .endpoint(endpoint)
                    .tipo(nombre)
                    .version("v1")
                    .build();
            api = apiRepository.save(api);
        }

        EmpresaApi empresaApi = EmpresaApi.builder()
                .empresa(empresa)
                .api(api)
                .habilitada(true)
                .build();

        empresaApiRepository.save(empresaApi);
        log.info("API {} asociada a la empresa {}", api.getNombre(), empresa.getName());
    }
}

