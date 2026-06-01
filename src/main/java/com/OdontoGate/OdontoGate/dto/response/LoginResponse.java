package com.OdontoGate.OdontoGate.dto.response;

import lombok.Data;
import com.OdontoGate.OdontoGate.model.UserType;

@Data
public class LoginResponse {
    private UserType userType;
}
