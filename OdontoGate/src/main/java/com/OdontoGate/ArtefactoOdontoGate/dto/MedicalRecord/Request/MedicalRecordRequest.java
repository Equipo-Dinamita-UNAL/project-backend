package com.OdontoGate.ArtefactoOdontoGate.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MedicalRecordRequest {

    @NotNull(message = "El ID del paciente es obligatorio")
    private Integer patientId;

    @NotBlank(message = "El diagnóstico es obligatorio")
    private String diagnosis;

    private String treatment;

    private String observations;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime date;
}