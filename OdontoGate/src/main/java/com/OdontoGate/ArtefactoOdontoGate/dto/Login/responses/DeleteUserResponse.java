package com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses;

import lombok.Data;

/**
 * Define el contrato publico de DeleteUserResponse.
 */
@Data
public class DeleteUserResponse {
    private String email;
    private String mensaje;
}
