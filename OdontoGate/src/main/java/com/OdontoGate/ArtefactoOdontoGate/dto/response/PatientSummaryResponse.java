package com.OdontoGate.ArtefactoOdontoGate.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PatientSummaryResponse {

    private Integer id;
    private String name;
    private String lastname;
    private String email;
    private String phone;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDate birthDate;
    private String bloodType;
    private String allergies;
    private String address;
}
