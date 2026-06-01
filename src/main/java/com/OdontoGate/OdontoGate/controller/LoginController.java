package com.OdontoGate.OdontoGate.controller;

import com.OdontoGate.OdontoGate.dto.request.ChangePasswordRequest;
import com.OdontoGate.OdontoGate.dto.request.LoginRequest;
import com.OdontoGate.OdontoGate.dto.response.ChangePasswordResponse;
import com.OdontoGate.OdontoGate.dto.response.LoginResponse;

import com.OdontoGate.OdontoGate.service.LoginService;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService){
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {
        
        LoginResponse response = loginService.login(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<ChangePasswordResponse> changePassword(
            @RequestBody ChangePasswordRequest request) {
        
        ChangePasswordResponse response = loginService.changePassword(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
