package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.response.AdministratorSummaryResponse;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.DoctorSummaryResponse;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.PatientSummaryResponse;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.RegisteredUsersResponse;
import com.OdontoGate.ArtefactoOdontoGate.service.AdminService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/registered-users")
    public ResponseEntity<RegisteredUsersResponse> getRegisteredUsers() {
        return ResponseEntity.ok(adminService.getRegisteredUsers());
    }

    @GetMapping("/registered-users/active/patients")
    public ResponseEntity<List<PatientSummaryResponse>> getActiveRegisteredPatients() {
        return ResponseEntity.ok(adminService.getActiveRegisteredPatients());
    }

    @GetMapping("/registered-users/active/doctors")
    public ResponseEntity<List<DoctorSummaryResponse>> getActiveRegisteredDoctors() {
        return ResponseEntity.ok(adminService.getActiveRegisteredDoctors());
    }

    @GetMapping("/registered-users/active/administrators")
    public ResponseEntity<List<AdministratorSummaryResponse>> getActiveRegisteredAdministrators() {
        return ResponseEntity.ok(adminService.getActiveRegisteredAdministrators());
    }

    @GetMapping("/registered-users/inactive/patients")
    public ResponseEntity<List<PatientSummaryResponse>> getInactiveRegisteredPatients() {
        return ResponseEntity.ok(adminService.getInactiveRegisteredPatients());
    }

    @GetMapping("/registered-users/inactive/doctors")
    public ResponseEntity<List<DoctorSummaryResponse>> getInactiveRegisteredDoctors() {
        return ResponseEntity.ok(adminService.getInactiveRegisteredDoctors());
    }

    @GetMapping("/registered-users/inactive/administrators")
    public ResponseEntity<List<AdministratorSummaryResponse>> getInactiveRegisteredAdministrators() {
        return ResponseEntity.ok(adminService.getInactiveRegisteredAdministrators());
    }

    @GetMapping("/registered-users/active")
    public ResponseEntity<RegisteredUsersResponse> getActiveRegisteredUsers() {
        return ResponseEntity.ok(adminService.getActiveRegisteredUsers());
    }

    @GetMapping("/registered-users/inactive")
    public ResponseEntity<RegisteredUsersResponse> getInactiveRegisteredUsers() {
        return ResponseEntity.ok(adminService.getInactiveRegisteredUsers());
    }
}
