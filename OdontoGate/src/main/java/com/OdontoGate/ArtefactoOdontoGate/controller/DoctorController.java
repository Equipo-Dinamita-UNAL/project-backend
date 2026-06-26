package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.response.DoctorPatientResponse;
import com.OdontoGate.ArtefactoOdontoGate.service.DoctorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/{doctorId}/patients")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasAuthority('DOCTOR_VER_PACIENTES_AGENDADOS')")
    public ResponseEntity<List<DoctorPatientResponse>> getScheduledPatients(
            @PathVariable Integer doctorId) {
        return ResponseEntity.ok(doctorService.getScheduledPatients(doctorId));
    }
}
