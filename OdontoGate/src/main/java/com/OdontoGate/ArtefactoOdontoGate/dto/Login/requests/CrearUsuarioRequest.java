package com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests;

import lombok.Data;

import java.time.LocalDate;

import com.OdontoGate.ArtefactoOdontoGate.model.UserType;

@Data
public class CrearUsuarioRequest {
    private String name;
    private String lastname;
    private String email;
    private String password;
    private String phone;
    private UserType userType;

    
    private LocalDate birthDate;
    private String bloodType;
    private String allergies;
    private String address;

    
    private String specialty;
    private String medicalLicense;
    private String photoUrl;

    
    private String position;
}
