package com.OdontoGate.ArtefactoOdontoGate.dto.request;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Define el contrato publico de AppointmentUpdateRequest.
 */
@Data
public class AppointmentUpdateRequest {

    private LocalDate date;
    private LocalTime time;
    private Integer doctorScheduleId;
    private String reason;
    private Integer modifiedBy;
}