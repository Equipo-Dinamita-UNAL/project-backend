package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.CrearUsuarioRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.DeleteUserRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.DeleteUserResponse;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.UsuarioCreadoResponse;
import com.OdontoGate.ArtefactoOdontoGate.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

        public UserController(UserService userService) {

        this.userService = userService;
    }

        @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<UsuarioCreadoResponse> createUser(
            @Valid @RequestBody CrearUsuarioRequest request) {

        UsuarioCreadoResponse response = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

        @DeleteMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<DeleteUserResponse> deleteUser(
            @Valid @RequestBody DeleteUserRequest request) {

        DeleteUserResponse response = userService.deleteUser(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
