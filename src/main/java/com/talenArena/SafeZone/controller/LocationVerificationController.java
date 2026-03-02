package com.talenArena.SafeZone.controller;

import com.talenArena.SafeZone.dto.LocationLastRequest;
import com.talenArena.SafeZone.dto.LocationLastResponse;
import com.talenArena.SafeZone.dto.LocationVerificationRequest;
import com.talenArena.SafeZone.service.LocationVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.talenArena.SafeZone.dto.Device;
@RestController
@RequestMapping("/api/location-verification")
public class LocationVerificationController {
    @Autowired
    private LocationVerificationService locationVerificationService;

    @PostMapping("/verify")
    public ResponseEntity<String> verifyLocation(@RequestBody LocationVerificationRequest request) {
        return locationVerificationService.verifyLocation(request);
    }

    @GetMapping("/last-location")
    public LocationLastResponse getLastLocation(@RequestParam String phoneNumber, @RequestParam Integer maxAge) {
        LocationLastRequest request = new LocationLastRequest();
        Device device = new Device();
        device.setPhoneNumber(phoneNumber);
        request.setDevice(device);
        request.setMaxAge(maxAge);
        return locationVerificationService.getLastLocation(request);
    }
}
