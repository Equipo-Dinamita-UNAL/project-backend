package com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String email;
    private String newPassword;
}
