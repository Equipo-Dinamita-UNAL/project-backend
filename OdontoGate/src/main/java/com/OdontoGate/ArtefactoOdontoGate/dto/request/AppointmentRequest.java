package com.OdontoGate.ArtefactoOdontoGate.dto.request;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequest {

    private Integer patientId;
    private Integer doctorId;
    private LocalDate date;
    private LocalTime time;
    private Integer doctorScheduleId;
    private String status;
    private String reason;
    private Integer modifiedBy;
}