package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.ChangePasswordResponse;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.LoginResponse;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.ChangePasswordRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.LoginRequest;


import com.OdontoGate.ArtefactoOdontoGate.model.User;
import com.OdontoGate.ArtefactoOdontoGate.model.UserType;


import com.OdontoGate.ArtefactoOdontoGate.repository.UserRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.AdministratorRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.DoctorRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.PatientRepository;


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
        userRepository.save(user);


        response.setMensaje("Contraseña actualizada exitosamente.");
        return response;
    }
}