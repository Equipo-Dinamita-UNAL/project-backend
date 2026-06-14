package com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses;

import lombok.Data;
import com.OdontoGate.ArtefactoOdontoGate.model.UserType;

/**
 * Define el contrato publico de LoginResponse.
 */
@Data
public class LoginResponse {
    private UserType userType;
}
