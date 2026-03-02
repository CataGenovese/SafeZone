package com.talenArena.SafeZone.controller;

import com.talenArena.SafeZone.dto.KycMatchResponse;
import com.talenArena.SafeZone.dto.PersonInfoRequest;
import com.talenArena.SafeZone.dto.PersonInfoResponse;
import com.talenArena.SafeZone.service.KYCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kyc-fill-in")
public class KnownYourClientController {

    @Autowired
    private KYCService kycService;

    @PostMapping("/person-info")
    public PersonInfoResponse getPersonInfo(@RequestBody PersonInfoRequest request) {
        return kycService.getPersonInfo(request);
    }

    @PostMapping("/kyc-match")
    public KycMatchResponse kycMatch(@RequestBody PersonInfoResponse request) {
        return kycService.kycMatch(request);
    }
}
