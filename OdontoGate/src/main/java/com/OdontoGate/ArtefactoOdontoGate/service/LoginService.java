package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.ChangePasswordResponse;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.LoginResponse;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.ChangePasswordRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.LoginRequest;


import com.OdontoGate.ArtefactoOdontoGate.model.Privilege;
import com.OdontoGate.ArtefactoOdontoGate.model.Role;
import com.OdontoGate.ArtefactoOdontoGate.model.User;
import com.OdontoGate.ArtefactoOdontoGate.model.UserType;


import com.OdontoGate.ArtefactoOdontoGate.repository.UserRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.AdministratorRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.DoctorRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.PatientRepository;
import com.OdontoGate.ArtefactoOdontoGate.security.JwtService;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;


@Service
public class LoginService {

    private final UserRepository userRepository;
    private final AdministratorRepository administratorRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final JwtService jwtService;

        public LoginService(UserRepository userRepository, AdministratorRepository administratorRepository,
                        PatientRepository patientRepository, DoctorRepository doctorRepository,
                        JwtService jwtService) {

        this.userRepository = userRepository;
        this.administratorRepository = administratorRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.jwtService = jwtService;
    }

    @Transactional(readOnly = true)
        public LoginResponse login(LoginRequest request){

        LoginResponse response = new LoginResponse();

        User user = findUserByCredentials(request);

        if(user == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Credenciales inválidas"
            );
        }
        response.setUserId(user.getId());
        setUserType(response, user.getId());
        setRoleAndPrivileges(response, user);
        response.setToken(jwtService.generateToken(user));

        return response;
    }

    private User findUserByCredentials(LoginRequest request) {
        return userRepository.findByEmailAndPassword(
                request.getEmail(),
                request.getPassword()
        );
    }

    private void setUserType(LoginResponse response, Integer userId) {
        boolean administrator = administratorRepository.existsById(userId);

        if(administrator){
            response.setUserType(UserType.ADMINISTRATOR);
            return;
        }

        boolean doctor = doctorRepository.existsById(userId);

        if(doctor){
            response.setUserType(UserType.DOCTOR);
            return;
        }

        boolean patient = patientRepository.existsById(userId);

        if(patient){
            response.setUserType(UserType.PATIENT);
        }
    }

    private void setRoleAndPrivileges(LoginResponse response, User user) {
        Role role = user.getRole();

        if (role == null) {
            response.setRole(null);
            response.setPrivileges(Collections.emptyList());
            return;
        }

        response.setRole(role.getNombre());
        response.setPrivileges(getPrivilegeNames(role));
    }

    private List<String> getPrivilegeNames(Role role) {
        return role.getPrivileges()
                .stream()
                .map(Privilege::getNombre)
                .toList();
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
