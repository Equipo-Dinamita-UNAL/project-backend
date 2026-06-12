package com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Responses;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MedicalRecordResponse {

    private Integer id;
    private Integer patientId;
    private String diagnosis;
    private String treatment;
    private String observations;
    private LocalDateTime date;
    private LocalDateTime createdAt;
}