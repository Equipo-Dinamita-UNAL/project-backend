package com.OdontoGate.OdontoGate.dto.response;

import lombok.Data;
import java.time.LocalTime;

@Data
public class ScheduleResponse {

    private Integer id;
    private String doctorName;
    private String weekday;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isAvailable;
}