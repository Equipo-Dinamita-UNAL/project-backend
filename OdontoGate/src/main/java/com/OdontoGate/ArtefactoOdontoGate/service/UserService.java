package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.CrearUsuarioRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.UsuarioCreadoResponse;

import com.OdontoGate.ArtefactoOdontoGate.model.Administrator;
import com.OdontoGate.ArtefactoOdontoGate.model.Doctor;
import com.OdontoGate.ArtefactoOdontoGate.model.Patient;
import com.OdontoGate.ArtefactoOdontoGate.model.User;
//import com.OdontoGate.ArtefactoOdontoGate.model.UserType;

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

    User user;

    switch (request.getUserType()) {
        case DOCTOR:
            Doctor doctor = new Doctor();
            doctor.setSpeciality(request.getSpecialty());
            doctor.setMedicalLicense(request.getMedicalLicense());
            doctor.setPhotoUrl(request.getPhotoUrl());
            user = doctor;
            break;

        case PATIENT:
            Patient patient = new Patient();
            patient.setBirthDate(request.getBirthDate());
            patient.setBloodType(request.getBloodType());
            patient.setAllergies(request.getAllergies());
            patient.setAddress(request.getAddress());
            user = patient;
            break;
            

        case ADMINISTRATOR:
            Administrator administrator = new Administrator();
            administrator.setPosition(request.getPosition());
            user = administrator;
            break;

        default:
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de usuario inválido");
    }

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
    response.setUserType(request.getUserType());

    return response;
}
}