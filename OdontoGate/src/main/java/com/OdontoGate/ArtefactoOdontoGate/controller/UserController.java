package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.CrearUsuarioRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.UsuarioCreadoResponse;
//import com.OdontoGate.ArtefactoOdontoGate.model.User;
import com.OdontoGate.ArtefactoOdontoGate.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
//import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UsuarioCreadoResponse> createUser(
            @RequestBody CrearUsuarioRequest request) {
        System.out.println("ENTRÓ AL POST NUEVO CON DTO");
        UsuarioCreadoResponse response = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    }
