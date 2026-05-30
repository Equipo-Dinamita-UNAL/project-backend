package com.OdontoGate.OdontoGate.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentResponse {

    private Integer id;
    private String patientName;
    private String doctorName;
    private LocalDate date;
    private LocalTime time;
    private String status;
    private String reason;
}
