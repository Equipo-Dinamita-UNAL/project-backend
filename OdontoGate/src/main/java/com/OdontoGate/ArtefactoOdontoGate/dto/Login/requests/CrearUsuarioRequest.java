package com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests;

import com.OdontoGate.ArtefactoOdontoGate.model.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CrearUsuarioRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String lastname;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotBlank
    private String phone;

    @NotNull
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
