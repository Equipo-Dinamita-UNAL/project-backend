package com.OdontoGate.ArtefactoOdontoGate.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class DoctorPatientResponse {

    private Integer id;
    private String name;
    private String lastname;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String bloodType;
    private String allergies;
    private String address;
    private List<AppointmentResponse> appointments;
}
