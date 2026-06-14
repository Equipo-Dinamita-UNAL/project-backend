package com.OdontoGate.ArtefactoOdontoGate.dto.response;

import lombok.Data;

/**
 * Define el contrato publico de DeleteUserResponse.
 */
@Data
public class DeleteUserResponse {
    private String email;
    private String mensaje;
}
