package com.OdontoGate.ArtefactoOdontoGate.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    @NotNull
    @Positive
    private Integer appointmentId;

    @NotNull
    @PositiveOrZero
    private BigDecimal amount;

    @NotBlank
    private String method;
}
