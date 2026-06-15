package com.OdontoGate.ArtefactoOdontoGate.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ReceiptRequest {
    @NotNull
    @Positive
    private Integer paymentId;

    @NotBlank
    private String type;

}
