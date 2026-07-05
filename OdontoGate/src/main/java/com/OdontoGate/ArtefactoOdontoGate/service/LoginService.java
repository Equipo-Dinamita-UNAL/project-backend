package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.ChangePasswordRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.LoginRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.ChangePasswordResponse;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.LoginResponse;
import com.OdontoGate.ArtefactoOdontoGate.model.Privilege;
import com.OdontoGate.ArtefactoOdontoGate.model.Role;
import com.OdontoGate.ArtefactoOdontoGate.model.User;
import com.OdontoGate.ArtefactoOdontoGate.model.UserType;
import com.OdontoGate.ArtefactoOdontoGate.repository.AdministratorRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.DoctorRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.PatientRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.UserRepository;
import com.OdontoGate.ArtefactoOdontoGate.security.JwtService;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final AdministratorRepository administratorRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final JwtService jwtService;

    public LoginService(UserRepository userRepository,
                        AdministratorRepository administratorRepository,
                        PatientRepository patientRepository,
                        DoctorRepository doctorRepository,
                        JwtService jwtService) {
        this.userRepository = userRepository;
        this.administratorRepository = administratorRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.jwtService = jwtService;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        LoginResponse response = new LoginResponse();
        User user = findUserByCredentials(request);

        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Credenciales inválidas"
            );
        }

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Esta cuenta ha sido desactivada. Contacta a la administraciÃ³n de la clÃ­nica."
            );
        }

        response.setUserId(user.getId());
        setUserType(response, user.getId());
        setRoleAndPrivileges(response, user);
        response.setToken(jwtService.generateToken(user));
        return response;
    }

    public ChangePasswordResponse changePassword(
            ChangePasswordRequest request,
            Integer currentUserId) {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario autenticado no encontrado."));

        if (!Objects.equals(request.getOldPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "La contraseÃ±a actual no es correcta.");
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);

        ChangePasswordResponse response = new ChangePasswordResponse();
        response.setMensaje("ContraseÃ±a actualizada exitosamente.");
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

        if (administrator) {
            response.setUserType(UserType.ADMINISTRATOR);
            return;
        }

        boolean doctor = doctorRepository.existsById(userId);

        if (doctor) {
            response.setUserType(UserType.DOCTOR);
            return;
        }

        boolean patient = patientRepository.existsById(userId);

        if (patient) {
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
}
