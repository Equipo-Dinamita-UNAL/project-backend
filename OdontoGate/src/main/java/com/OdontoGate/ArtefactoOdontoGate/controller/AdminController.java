package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.response.RegisteredUsersResponse;
import com.OdontoGate.ArtefactoOdontoGate.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/registered-users")
    public ResponseEntity<RegisteredUsersResponse> getRegisteredPatientsAndDoctors() {
        return ResponseEntity.ok(adminService.getRegisteredPatientsAndDoctors());
    }

    @GetMapping("/registered-users/active")
    public ResponseEntity<RegisteredUsersResponse> getActiveRegisteredPatientsAndDoctors() {
        return ResponseEntity.ok(adminService.getActiveRegisteredPatientsAndDoctors());
    }

    @GetMapping("/registered-users/inactive")
    public ResponseEntity<RegisteredUsersResponse> getInactiveRegisteredPatientsAndDoctors() {
        return ResponseEntity.ok(adminService.getInactiveRegisteredPatientsAndDoctors());
    }
}
