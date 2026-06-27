package com.OdontoGate.ArtefactoOdontoGate.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReceiptResponse {
    private Integer id;
    private String receiptNumber;
    private String type;
    private String treatmentReason;
    private String doctorName;
    private String doctorLastname;
    private String patientName;
    private String patientLastname;
    private BigDecimal amount;
    private String paymentMethod;
    private String gatewayReference;
    private String pdfUrl;
    private LocalDateTime issueDate;
    private LocalDateTime createdAt;
}
