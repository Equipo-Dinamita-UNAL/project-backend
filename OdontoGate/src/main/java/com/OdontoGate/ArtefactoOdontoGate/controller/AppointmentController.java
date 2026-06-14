package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.AppointmentRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.AppointmentResponse;
import com.OdontoGate.ArtefactoOdontoGate.service.AppointmentService;
import com.OdontoGate.ArtefactoOdontoGate.dto.request.AppointmentUpdateRequest;
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

/**
 * Define el contrato publico de AppointmentController.
 */
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // PUT /api/appointments/1
    /**
     * Ejecuta la operacion publica update.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponse> update(
            @PathVariable Integer id,
            @RequestBody AppointmentUpdateRequest request) {
        return ResponseEntity.ok(appointmentService.update(id, request));
    }

    // PATCH /api/appointments/1/cancel
    /**
     * Ejecuta la operacion publica cancel.
     */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponse> cancel(@PathVariable Integer id) {
        return ResponseEntity.ok(appointmentService.cancel(id));
    }

    // GET /api/appointments
    /**
     * Ejecuta la operacion publica getAll.
     */
    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> getAll() {
        return ResponseEntity.ok(appointmentService.getAll());
    }

    // GET /api/appointments/patient/1
    /**
     * Ejecuta la operacion publica getByPatient.
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponse>> getByPatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(appointmentService.getByPatient(patientId));
    }

    // GET /api/appointments/doctor/1
    /**
     * Ejecuta la operacion publica getByDoctor.
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponse>> getByDoctor(@PathVariable Integer doctorId) {
        return ResponseEntity.ok(appointmentService.getByDoctor(doctorId));
    }

    // POST /api/appointments
    /**
     * Ejecuta la operacion publica create.
     */
    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@RequestBody AppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.create(request));
    }

    // DELETE /api/appointments/1
    /**
     * Ejecuta la operacion publica delete.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}