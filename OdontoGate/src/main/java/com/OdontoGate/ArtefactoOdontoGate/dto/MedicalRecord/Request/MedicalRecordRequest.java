package com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Request;

import com.OdontoGate.ArtefactoOdontoGate.dto.validation.OnCreate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MedicalRecordRequest {

    @NotNull(groups = OnCreate.class)
    @Positive
    private Integer patientId;

    @NotBlank
    private String diagnosis;

    @NotBlank
    private String treatment;

    private String observations;

    @NotNull
    private LocalDateTime date;
}
