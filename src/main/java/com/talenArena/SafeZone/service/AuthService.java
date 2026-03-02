package com.talenArena.SafeZone.service;

import com.talenArena.SafeZone.dto.Auth.AuthRequestDto;
import com.talenArena.SafeZone.dto.Auth.AuthResponseDto;
import com.talenArena.SafeZone.dto.Kyc.KycMatchResponse;
import com.talenArena.SafeZone.dto.Kyc.PersonInfoRequest;
import com.talenArena.SafeZone.dto.Location.Device;
import com.talenArena.SafeZone.dto.Location.LocationLastRequest;
import com.talenArena.SafeZone.dto.Location.LocationLastResponse;
import com.talenArena.SafeZone.dto.SimSwap.SimSwapResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final LocationVerificationService locationService;
    private final KYCService kycService;
    private final SimSwapService simSwapService;

    public AuthResponseDto authenticate(AuthRequestDto request) {
        log.info("Iniciando autenticación para phoneNumber: {}", request.getPhoneNumber());

        String phoneNumber = formatPhoneNumber(request.getPhoneNumber());

        LocationLastResponse locationResponse = verifyLocation(phoneNumber);
        KycMatchResponse kycResponse = verifyKyc(phoneNumber);
        SimSwapResponse simSwapResponse = verifySimSwap(phoneNumber);

        AuthResponseDto response = AuthResponseDto.builder()
                .locationLastResponse(locationResponse)
                .kycMatchResponse(kycResponse)
                .simSwapResponse(simSwapResponse)
                .build();

        log.info("Autenticación completada para phoneNumber: {}", request.getPhoneNumber());

        return response;
    }

    private LocationLastResponse verifyLocation(String phoneNumber) {
        try {
            log.info("Verificando ubicación...");
            LocationLastRequest locationRequest = new LocationLastRequest();
            Device device = new Device();
            device.setPhoneNumber(phoneNumber);
            locationRequest.setDevice(device);
            locationRequest.setMaxAge(3600);

            LocationLastResponse response = locationService.getLastLocation(locationRequest);
            log.info("Verificación de ubicación completada");
            return response;
        } catch (Exception e) {
            log.error("Error en verificación de ubicación: {}", e.getMessage());
            return new LocationLastResponse();
        }
    }


    private KycMatchResponse verifyKyc(String phoneNumber) {
        try {
            log.info("Verificando KYC...");
            PersonInfoRequest kycRequest = new PersonInfoRequest();
            kycRequest.setPhoneNumber(phoneNumber);

            kycService.getPersonInfo(kycRequest);

            KycMatchResponse response = new KycMatchResponse();
            log.info("Verificación KYC completada");
            return response;
        } catch (Exception e) {
            log.error("Error en verificación KYC: {}", e.getMessage());
            return new KycMatchResponse();
        }
    }


    private SimSwapResponse verifySimSwap(String phoneNumber) {
        try {
            log.info("Verificando SIM Swap...");
            SimSwapResponse response = simSwapService.checkSimSwap(phoneNumber, null);
            log.info("Verificación SIM Swap completada");
            return response;
        } catch (Exception e) {
            log.error("Error en verificación SIM Swap: {}", e.getMessage());
            return SimSwapResponse.builder()
                    .swapped(false)
                    .build();
        }
    }


    private String formatPhoneNumber(Long phoneNumber) {
        String phoneStr = String.valueOf(phoneNumber);

        if (!phoneStr.startsWith("+")) {
            phoneStr = "+" + phoneStr;
        }

        return phoneStr;
    }
}


