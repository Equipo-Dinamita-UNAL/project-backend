package com.OdontoGate.ArtefactoOdontoGate.dto.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Define el contrato publico de PaymentRequest.
 */
@Data
public class PaymentRequest {
    private Integer appointmentId;
    private BigDecimal amount;
    private String method;
}
