package com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests;

import lombok.Data;

@Data
public class CrearUsuarioRequest {
    private String name;
    private String lastname;
    private String email;
    private String password;
    private String phone;
}
