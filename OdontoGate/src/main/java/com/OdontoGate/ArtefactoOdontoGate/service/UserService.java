package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.CrearUsuarioRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests.DeleteUserRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.DeleteUserResponse;
import com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses.UsuarioCreadoResponse;

import com.OdontoGate.ArtefactoOdontoGate.model.Administrator;
import com.OdontoGate.ArtefactoOdontoGate.model.Doctor;
import com.OdontoGate.ArtefactoOdontoGate.model.Patient;
import com.OdontoGate.ArtefactoOdontoGate.model.Role;
import com.OdontoGate.ArtefactoOdontoGate.model.User;
import com.OdontoGate.ArtefactoOdontoGate.model.UserType;

import com.OdontoGate.ArtefactoOdontoGate.repository.UserRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.AdministratorRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.PatientRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.DoctorRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.RoleRepository;

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
    private final RoleRepository roleRepository;

        public UserService(UserRepository userRepository, AdministratorRepository administratorRepository,
                       PatientRepository patientRepository, DoctorRepository doctorRepository,
                       RoleRepository roleRepository) {

        this.userRepository = userRepository;
        this.administratorRepository = administratorRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.roleRepository = roleRepository;
    }

        public UsuarioCreadoResponse createUser(CrearUsuarioRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email ya está registrado");
        }

        User user = buildUser(request);

        fillCommonUserData(user, request);

        User savedUser = userRepository.save(user);

        return buildCreatedUserResponse(savedUser, request);
    }

    private User buildUser(CrearUsuarioRequest request) {
        switch (request.getUserType()) {
            case DOCTOR:
                return buildDoctor(request);

            case PATIENT:
                return buildPatient(request);


            case ADMINISTRATOR:
                return buildAdministrator(request);

            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de usuario invÃ¡lido");
        }
    }

    private Doctor buildDoctor(CrearUsuarioRequest request) {
        Doctor doctor = new Doctor();
        doctor.setSpeciality(request.getSpecialty());
        doctor.setMedicalLicense(request.getMedicalLicense());
        doctor.setPhotoUrl(request.getPhotoUrl());
        return doctor;
    }

    private Patient buildPatient(CrearUsuarioRequest request) {
        Patient patient = new Patient();
        patient.setBirthDate(request.getBirthDate());
        patient.setBloodType(request.getBloodType());
        patient.setAllergies(request.getAllergies());
        patient.setAddress(request.getAddress());
        return patient;
    }

    private Administrator buildAdministrator(CrearUsuarioRequest request) {
        Administrator administrator = new Administrator();
        administrator.setPosition(request.getPosition());
        return administrator;
    }

    private void fillCommonUserData(User user, CrearUsuarioRequest request) {
        user.setName(request.getName());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(findRole(request.getUserType()));
    }

    private Role findRole(UserType userType) {
        String roleName = getRoleName(userType);

        return roleRepository.findByNombre(roleName)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Rol no configurado en base de datos: " + roleName));
    }

    private String getRoleName(UserType userType) {
        return switch (userType) {
            case ADMINISTRATOR -> "administrator";
            case DOCTOR -> "doctor";
            case PATIENT -> "patient";
        };
    }

    private UsuarioCreadoResponse buildCreatedUserResponse(
            User savedUser,
            CrearUsuarioRequest request) {
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

        // Soft delete: en vez de borrar físicamente (lo cual falla si el usuario
        // tiene registros relacionados, como citas o historial clínico), se
        // desactiva la cuenta. Esto preserva la integridad referencial y el
        // historial clínico/de citas para auditoría y trazabilidad.
        user.setActive(false);
        userRepository.save(user);

        response.setMensaje("Usuario desactivado correctamente.");
        response.setEmail(request.getEmail());

        return response;
    }

}
