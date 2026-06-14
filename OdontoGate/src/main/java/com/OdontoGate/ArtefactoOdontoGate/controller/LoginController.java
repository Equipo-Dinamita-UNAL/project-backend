package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.ChangePasswordRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.LoginRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.ChangePasswordResponse;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.LoginResponse;

import com.OdontoGate.ArtefactoOdontoGate.service.LoginService;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;


/**
 * Define el contrato publico de LoginController.
 */
@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final LoginService loginService;

    /**
     * Ejecuta la operacion publica LoginController.
     */
    public LoginController(LoginService loginService){
        this.loginService = loginService;
    }

    /**
     * Ejecuta la operacion publica login.
     */
    @PostMapping
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        LoginResponse response = loginService.login(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Ejecuta la operacion publica changePassword.
     */
    @PostMapping("/change-password")
    public ResponseEntity<ChangePasswordResponse> changePassword(
            @RequestBody ChangePasswordRequest request) {

        ChangePasswordResponse response = loginService.changePassword(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}