package com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests;

import lombok.Data;

/**
 * Define el contrato publico de LoginRequest.
 */
@Data
public class LoginRequest {
    private String email;
    private String password;
}
