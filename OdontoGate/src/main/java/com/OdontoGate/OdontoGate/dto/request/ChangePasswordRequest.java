package com.OdontoGate.OdontoGate.dto.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String email;
    private String newPassword;
}
