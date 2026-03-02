package com.talenArena.SafeZone.controller;

import com.talenArena.SafeZone.dto.SimSwapRequest;
import com.talenArena.SafeZone.dto.SimSwapResponse;
import com.talenArena.SafeZone.service.SimSwapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/sim-swap")
@RequiredArgsConstructor
public class SimSwapController {

    private final SimSwapService simSwapService;

    @PostMapping("/check")
    public ResponseEntity<SimSwapResponse> checkSimSwap(@RequestBody SimSwapRequest request) {
        log.info("API Check SIM Swap solicitada para: {}", request.getPhoneNumber());

        try {
            SimSwapResponse response = simSwapService.checkSimSwap(
                request.getPhoneNumber(),
                request.getMaxAge()
            );
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("API SIM Swap devolvió error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}



