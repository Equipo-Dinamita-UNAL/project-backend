package com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests;

import lombok.Data;

/**
 * Define el contrato publico de ChangePasswordRequest.
 */
@Data
public class ChangePasswordRequest {
    private String email;
    private String newPassword;
}
