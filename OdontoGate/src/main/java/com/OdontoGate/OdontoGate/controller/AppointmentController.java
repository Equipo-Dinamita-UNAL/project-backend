package com.OdontoGate.OdontoGate.controller;

import com.OdontoGate.OdontoGate.dto.request.AppointmentRequest;
import com.OdontoGate.OdontoGate.dto.response.AppointmentResponse;
import com.OdontoGate.OdontoGate.service.AppointmentService;
import com.OdontoGate.OdontoGate.dto.request.AppointmentUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // PUT /api/appointments/1
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponse> update(
            @PathVariable Integer id,
            @RequestBody AppointmentUpdateRequest request) {
        return ResponseEntity.ok(appointmentService.update(id, request));
    }

    // PATCH /api/appointments/1/cancel
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponse> cancel(@PathVariable Integer id) {
        return ResponseEntity.ok(appointmentService.cancel(id));
    }

    // GET /api/appointments
    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> getAll() {
        return ResponseEntity.ok(appointmentService.getAll());
    }

    // GET /api/appointments/patient/1
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponse>> getByPatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(appointmentService.getByPatient(patientId));
    }

    // GET /api/appointments/doctor/1
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponse>> getByDoctor(@PathVariable Integer doctorId) {
        return ResponseEntity.ok(appointmentService.getByDoctor(doctorId));
    }

    // POST /api/appointments
    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@RequestBody AppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.create(request));
    }

    // DELETE /api/appointments/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}