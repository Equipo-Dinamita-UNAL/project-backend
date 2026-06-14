package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.CrearUsuarioRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.DeleteUserRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.DeleteUserResponse;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.UsuarioCreadoResponse;
import com.OdontoGate.ArtefactoOdontoGate.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

/**
 * Define el contrato publico de UserController.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Ejecuta la operacion publica UserController.
     */
    public UserController(UserService userService) {

        this.userService = userService;
    }

    /**
     * Ejecuta la operacion publica createUser.
     */
    @PostMapping
    public ResponseEntity<UsuarioCreadoResponse> createUser(
            @RequestBody CrearUsuarioRequest request) {

        UsuarioCreadoResponse response = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Ejecuta la operacion publica deleteUser.
     */
    @DeleteMapping
    public ResponseEntity<DeleteUserResponse> deleteUser(
            @RequestBody DeleteUserRequest request) {

        DeleteUserResponse response = userService.deleteUser(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}