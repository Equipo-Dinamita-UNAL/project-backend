package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.LoginRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.LoginResponse;

import com.OdontoGate.ArtefactoOdontoGate.service.LoginService;

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
}
