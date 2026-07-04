package com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MedicalDocumentResponse {

    private Integer id;
    private Integer patientId;
    private Integer medicalRecordId;
    private String fileName;
    private String originalName;
    private String fileType;
    private Long fileSize;
    private String description;
    private LocalDateTime createdAt;
}