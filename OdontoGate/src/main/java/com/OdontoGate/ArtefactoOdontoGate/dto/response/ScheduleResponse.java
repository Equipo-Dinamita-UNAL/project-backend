package com.OdontoGate.ArtefactoOdontoGate.dto.response;

import lombok.Data;
import java.time.LocalTime;

/**
 * Define el contrato publico de ScheduleResponse.
 */
@Data
public class ScheduleResponse {

    private Integer id;
    private String doctorName;
    private String weekday;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isAvailable;
}