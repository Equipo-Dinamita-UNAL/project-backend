package com.OdontoGate.ArtefactoOdontoGate.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.Data;



@Data
public class PaymentRequest {
    @NotNull
    @Positive
    private Integer appointmentId;

    @NotBlank
    private String method;
}
