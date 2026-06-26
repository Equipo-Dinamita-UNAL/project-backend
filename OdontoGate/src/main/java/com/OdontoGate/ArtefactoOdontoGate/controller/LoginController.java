package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.ChangePasswordRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.LoginRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.ChangePasswordResponse;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.LoginResponse;

import com.OdontoGate.ArtefactoOdontoGate.service.LoginService;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = loginService.login(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

        @PostMapping("/change-password")
    public ResponseEntity<ChangePasswordResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        ChangePasswordResponse response = loginService.changePassword(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
