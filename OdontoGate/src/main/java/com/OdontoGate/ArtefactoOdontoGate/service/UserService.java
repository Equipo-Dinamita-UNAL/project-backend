package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.CrearUsuarioRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.UsuarioCreadoResponse;
import com.OdontoGate.ArtefactoOdontoGate.model.User;
import com.OdontoGate.ArtefactoOdontoGate.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public UsuarioCreadoResponse createUser(CrearUsuarioRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email ya está registrado");
        }

        User user = new User();
        user.setName(request.getName());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        UsuarioCreadoResponse response = new UsuarioCreadoResponse();
        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setLastname(savedUser.getLastname());
        response.setEmail(savedUser.getEmail());
        response.setPhone(savedUser.getPhone());
        response.setActive(savedUser.getActive());

        return response;
    }
}