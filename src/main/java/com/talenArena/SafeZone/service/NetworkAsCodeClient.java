package com.talenArena.SafeZone.service;

import com.talenArena.SafeZone.models.Device;
import com.talenArena.SafeZone.models.DeviceIpv4Addr;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NetworkAsCodeClient {

    private final DeviceService deviceService;

    @Value("${network.as.code.token:}")
    private String token;

    /**
     * Obtiene un dispositivo basado en sus identificadores
     *
     * @param ipv4Address Dirección IPv4 del dispositivo
     * @param ipv6Address Dirección IPv6 del dispositivo
     * @param phoneNumber Número de teléfono del dispositivo
     * @return Device objeto que representa el dispositivo
     */
    public Device getDevice(DeviceIpv4Addr ipv4Address, String ipv6Address, String phoneNumber) {
        log.info("Obteniendo dispositivo con IPv4: {}, IPv6: {}, Teléfono: {}",
                ipv4Address, ipv6Address, phoneNumber);

        return Device.builder()
                .ipv4Address(ipv4Address)
                .ipv6Address(ipv6Address)
                .phoneNumber(phoneNumber)
                .build();
    }

    /**
     * Valida el token de autenticación
     *
     * @return true si el token está configurado
     */
    public boolean isTokenConfigured() {
        return token != null && !token.isEmpty();
    }
}

