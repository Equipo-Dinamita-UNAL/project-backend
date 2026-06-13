package com.OdontoGate.ArtefactoOdontoGate.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private Integer appointmentId;
    private BigDecimal amount;
    private String method;
}
