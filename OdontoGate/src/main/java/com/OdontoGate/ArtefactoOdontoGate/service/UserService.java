package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.CrearUsuarioRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.DeleteUserRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.DeleteUserResponse;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.UsuarioCreadoResponse;

import com.OdontoGate.ArtefactoOdontoGate.model.Administrator;
import com.OdontoGate.ArtefactoOdontoGate.model.Doctor;
import com.OdontoGate.ArtefactoOdontoGate.model.Patient;
import com.OdontoGate.ArtefactoOdontoGate.model.User;

import com.OdontoGate.ArtefactoOdontoGate.repository.UserRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.AdministratorRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.PatientRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.DoctorRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AdministratorRepository administratorRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public UserService(UserRepository userRepository, AdministratorRepository administratorRepository,
                       PatientRepository patientRepository, DoctorRepository doctorRepository) {

        this.userRepository = userRepository;
        this.administratorRepository = administratorRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
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



    public DeleteUserResponse deleteUser(DeleteUserRequest request){
        DeleteUserResponse response = new DeleteUserResponse();

        User user = userRepository.findByEmail(request.getEmail());

        if(user == null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Correo no asociado con ningún usuario."
            );
        }

        Integer userId = user.getId();

        boolean administrator = administratorRepository.existsById(userId);

        if(administrator){
            administratorRepository.deleteById(userId);
        }

        boolean patient = patientRepository.existsById(userId);

        if(patient){
            patientRepository.deleteById(userId);
        }

        boolean doctor = doctorRepository.existsById(userId);

        if(doctor){
            doctorRepository.deleteById(userId);
        }

        userRepository.deleteById(userId);


        response.setMensaje("Usuario eliminado correctamente.");
        response.setEmail(request.getEmail());

        return response;
    }

}