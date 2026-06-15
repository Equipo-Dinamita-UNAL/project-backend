package com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses;

import lombok.Data;
import com.OdontoGate.ArtefactoOdontoGate.model.UserType;

@Data
public class LoginResponse {
    private UserType userType;
}
