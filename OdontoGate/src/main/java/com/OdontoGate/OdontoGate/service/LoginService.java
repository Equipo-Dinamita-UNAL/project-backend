package com.OdontoGate.OdontoGate.service;

import com.OdontoGate.OdontoGate.dto.response.ChangePasswordResponse;
import com.OdontoGate.OdontoGate.dto.response.LoginResponse;
import com.OdontoGate.OdontoGate.dto.request.ChangePasswordRequest;
import com.OdontoGate.OdontoGate.dto.request.LoginRequest;


import com.OdontoGate.OdontoGate.model.User;
import com.OdontoGate.OdontoGate.model.UserType;


import com.OdontoGate.OdontoGate.repository.UserRepository;
import com.OdontoGate.OdontoGate.repository.AdministratorRepository;
import com.OdontoGate.OdontoGate.repository.DoctorRepository;
import com.OdontoGate.OdontoGate.repository.PatientRepository;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class LoginService {

    private final UserRepository userRepository;
    private final AdministratorRepository administratorRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public LoginService(UserRepository userRepository, AdministratorRepository administratorRepository,
        PatientRepository patientRepository, DoctorRepository doctorRepository) {

        this.userRepository = userRepository;
        this.administratorRepository = administratorRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public LoginResponse login(LoginRequest request){

        LoginResponse response = new LoginResponse(); 

        User user = userRepository
            .findByEmailAndPassword(
                    request.getEmail(),
                    request.getPassword()
            );

        if(user == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Credenciales inválidas"
        );
}
        Integer userId = user.getId();

        boolean administrator = administratorRepository.existsById(userId);
        

        if(administrator){
            response.setUserType(UserType.ADMINISTRATOR);
            return response;
        } 

        boolean doctor = doctorRepository.existsById(userId);

        if(doctor){
            response.setUserType(UserType.DOCTOR);
            return response;
        } 

        boolean patient = patientRepository.existsById(userId);

        if(patient){
            response.setUserType(UserType.PATIENT);
            return response;
        } 

        return response;
    }

    public ChangePasswordResponse changePassword(ChangePasswordRequest request){
        ChangePasswordResponse response = new ChangePasswordResponse();

        User user = userRepository.findByEmail(request.getEmail());

        if(user == null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Correo no asociado con ningún usuario."
        );
        }

        user.setPassword(request.getNewPassword());
        User savedUser = userRepository.save(user);

        if(savedUser == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se pudo cambiar la contraseña."
        );
        }

        response.setMensaje("Contraseña actualizada exitosamente.");
        return response;
    }
}
