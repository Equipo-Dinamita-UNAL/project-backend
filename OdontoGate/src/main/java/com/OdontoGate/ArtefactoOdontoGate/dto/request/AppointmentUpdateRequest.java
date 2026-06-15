package com.OdontoGate.ArtefactoOdontoGate.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentUpdateRequest {

    @Future
    private LocalDate date;

    private LocalTime time;

    @Positive
    private Integer doctorScheduleId;

    private String reason;

    @Positive
    private Integer modifiedBy;
}
