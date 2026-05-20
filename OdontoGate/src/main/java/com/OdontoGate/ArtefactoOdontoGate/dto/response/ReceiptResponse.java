package com.OdontoGate.ArtefactoOdontoGate.dto.response;

import java.time.LocalDateTime;

public class ReceiptResponse {
    private Integer id;
    private String type;
    private String pdfUrl;
    private LocalDateTime issueDate;
    private LocalDateTime createdAt;
}
