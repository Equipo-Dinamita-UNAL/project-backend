package com.OdontoGate.ArtefactoOdontoGate.dto.request;

import java.math.BigDecimal;

public class PaymentRequest {
    private Integer appointmentPatientId;
    private Integer appointmentDoctorId;
    private Integer patientId;
    private BigDecimal amount;
    private String method;
}
