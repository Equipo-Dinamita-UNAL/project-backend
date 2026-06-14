package com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses;
import com.OdontoGate.ArtefactoOdontoGate.model.UserType;

import lombok.Data;

/**
 * Define el contrato publico de UsuarioCreadoResponse.
 */
@Data
public class UsuarioCreadoResponse {
    private Integer id;
    private String name;
    private String lastname;
    private String email;
    private String phone;
    private Boolean active;
    private UserType userType;
}