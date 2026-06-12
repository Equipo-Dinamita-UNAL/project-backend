package com.OdontoGate.ArtefactoOdontoGate.dto.request;

import lombok.Data;
import java.time.LocalTime;

@Data
public class ScheduleRequest {

    private Integer doctorId;
    private String weekday;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isAvailable;
}