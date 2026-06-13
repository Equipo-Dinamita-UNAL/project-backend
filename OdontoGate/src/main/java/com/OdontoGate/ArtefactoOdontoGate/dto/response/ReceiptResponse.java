package com.OdontoGate.ArtefactoOdontoGate.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReceiptResponse {
    private Integer id;
    private String receiptNumber;
    private String type;
    private String pdfUrl;
    private LocalDateTime issueDate;
    private LocalDateTime createdAt;
}
