package com.talenArena.SafeZone.service;


import com.talenArena.SafeZone.dto.LoginDto;

public class LoginService {

    public LoginDto login(){
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("");
        loginDto.setPassword("");
        return loginDto;
    }
}
