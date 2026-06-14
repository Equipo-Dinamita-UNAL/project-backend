package com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Request;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Define el contrato publico de MedicalRecordRequest.
 */
@Data
public class MedicalRecordRequest {

    private Integer patientId;

    private String diagnosis;

    private String treatment;

    private String observations;

    private LocalDateTime date;
}