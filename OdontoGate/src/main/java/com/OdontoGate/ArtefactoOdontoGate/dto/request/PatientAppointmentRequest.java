package com.OdontoGate.ArtefactoOdontoGate.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
public class PatientAppointmentRequest {

    @NotNull
    @Future
    private LocalDate date;

    @NotNull
    private LocalTime time;

    @NotNull
    @Positive
    private Integer doctorScheduleId;

    @NotBlank
    private String reason;
}
