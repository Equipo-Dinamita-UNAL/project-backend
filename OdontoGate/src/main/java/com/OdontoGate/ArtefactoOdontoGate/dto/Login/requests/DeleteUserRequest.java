package com.OdontoGate.ArtefactoOdontoGate.dto.Login.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeleteUserRequest {
    @NotBlank
    @Email
    private String email;
}
