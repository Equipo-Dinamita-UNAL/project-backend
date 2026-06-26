package com.OdontoGate.ArtefactoOdontoGate.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private Integer id;
    private BigDecimal amount;
    private String method;
    private String status;
    private String gatewayReference;
    private LocalDateTime createdAt;
    private String patientName;
    private String patientLastname;
    private String checkoutUrl;
}
