package com.talenArena.SafeZone.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.talenArena.SafeZone.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LocationVerificationService {
    @Value("${network.as.code.api.url}")
    private String apiUrl;

    @Value("${network.as.code.rapidapi.host}")
    private String rapidApiHost;

    @Value("${network.as.code.rapidapi.key}")
    private String rapidApiKey;

    @Value("${network.as.code.mock.enabled:false}")
    private boolean mockEnabled;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResponseEntity<String> verifyLocation(LocationVerificationRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-rapidapi-host", rapidApiHost);
        headers.set("x-rapidapi-key", rapidApiKey);
        try {
            String jsonBody = objectMapper.writeValueAsString(request);
            System.out.println("JSON enviado a Nokia API: " + jsonBody);
            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
            return restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error serializando JSON");
        }
    }

    public LocationLastResponse getLastLocation(LocationLastRequest request) {
        if (mockEnabled) {
            LocationLastResponse response = new LocationLastResponse();
            response.setLastLocationTime("2026-03-02T11:40:59.969867Z");
            Area area = new Area();
            area.setAreaType("CIRCLE");
            Center center = new Center();
            center.setLatitude(47.48627616952785);
            center.setLongitude(19.07915612501993);
            area.setCenter(center);
            area.setRadius(1000);
            response.setArea(area);
            return response;
        }
        // Aquí iría la llamada real a la API externa si no es mock
        // Puedes implementar la lógica real según la documentación de la API
        return null;
    }
}
