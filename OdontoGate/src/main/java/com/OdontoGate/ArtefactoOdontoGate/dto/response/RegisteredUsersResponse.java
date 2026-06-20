package com.OdontoGate.ArtefactoOdontoGate.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredUsersResponse {

    private List<PatientSummaryResponse> patients;
    private List<DoctorSummaryResponse> doctors;
}
