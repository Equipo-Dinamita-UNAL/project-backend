package com.OdontoGate.ArtefactoOdontoGate.dto.Login.responses;

import java.util.List;
import lombok.Data;
import com.OdontoGate.ArtefactoOdontoGate.model.UserType;

@Data
public class LoginResponse {
    private Integer userId;
    private UserType userType;
    private String role;
    private List<String> privileges;
    private String token;
}
