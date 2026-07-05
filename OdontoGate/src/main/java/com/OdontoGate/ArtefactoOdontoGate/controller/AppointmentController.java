package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.AppointmentRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.AppointmentResponse;
import com.OdontoGate.ArtefactoOdontoGate.service.AppointmentService;
import com.OdontoGate.ArtefactoOdontoGate.service.CurrentUserService;
import com.OdontoGate.ArtefactoOdontoGate.dto.request.AppointmentUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final CurrentUserService currentUserService;

    // PUT /api/appointments/1
        @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasAuthority('PATIENT_MODIFICAR_CITA')")
    public ResponseEntity<AppointmentResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody AppointmentUpdateRequest request) {
        return ResponseEntity.ok(appointmentService.update(
                id,
                request,
                currentUserService.getCurrentUserId(),
                currentUserService.isPatient()));
    }

    // PATCH /api/appointments/1/cancel
        @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasAuthority('PATIENT_MODIFICAR_CITA')")
    public ResponseEntity<AppointmentResponse> cancel(@PathVariable Integer id) {
        return ResponseEntity.ok(appointmentService.cancel(
                id,
                currentUserService.getCurrentUserId(),
                currentUserService.isPatient()));
    }

    // GET /api/appointments
        @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<List<AppointmentResponse>> getAll() {
        return ResponseEntity.ok(appointmentService.getAll());
    }

    // GET /api/appointments/patient/1
        @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('ADMINISTRATOR') "
            + "or (hasRole('PATIENT') and #patientId == authentication.principal.id)")
    public ResponseEntity<List<AppointmentResponse>> getByPatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(appointmentService.getByPatient(
                patientId,
                currentUserService.getCurrentUserId(),
                currentUserService.isPatient()));
    }

    // GET /api/appointments/doctor/1
        @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('ADMINISTRATOR') "
            + "or (hasRole('DOCTOR') and #doctorId == authentication.principal.id)")
    public ResponseEntity<List<AppointmentResponse>> getByDoctor(@PathVariable Integer doctorId) {
        return ResponseEntity.ok(appointmentService.getByDoctor(
                doctorId,
                currentUserService.getCurrentUserId(),
                currentUserService.isDoctor()));
    }

    // POST /api/appointments
        @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasAuthority('PATIENT_CREAR_CITA')")
    public ResponseEntity<AppointmentResponse> create(
            @Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.create(
                request,
                currentUserService.getCurrentUserId(),
                currentUserService.isPatient()));
    }

    // DELETE /api/appointments/1
        @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasAuthority('PATIENT_ELIMINAR_CITA')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        appointmentService.delete(
                id,
                currentUserService.getCurrentUserId(),
                currentUserService.isPatient());
        return ResponseEntity.noContent().build();
    }
}
