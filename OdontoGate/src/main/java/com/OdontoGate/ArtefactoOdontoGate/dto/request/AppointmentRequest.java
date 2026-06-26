package com.OdontoGate.ArtefactoOdontoGate.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequest {

    @NotNull
    @Positive
    private Integer patientId;

    @NotNull
    @Positive
    private Integer doctorId;

    @NotNull
    @Future
    private LocalDate date;

    @NotNull
    private LocalTime time;

    @NotNull
    @Positive
    private Integer doctorScheduleId;

    @NotBlank
    private String status;

    @NotBlank
    private String reason;

    @NotNull
    @Positive
    private Integer modifiedBy;
}
