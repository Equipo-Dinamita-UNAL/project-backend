package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.AppointmentRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.request.PatientAppointmentRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.AppointmentResponse;
import com.OdontoGate.ArtefactoOdontoGate.service.AppointmentService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private static final String DEFAULT_APPOINTMENT_STATUS = "pendiente";

    private final AppointmentService appointmentService;

    @GetMapping("/{patientId}/appointments")
    @PreAuthorize("hasRole('ADMINISTRATOR') "
            + "or (hasRole('PATIENT') and #patientId == authentication.principal.id)")
    public ResponseEntity<List<AppointmentResponse>> getAppointments(
            @PathVariable Integer patientId) {
        return ResponseEntity.ok(appointmentService.getByPatient(patientId));
    }

    @PostMapping("/{patientId}/doctors/{doctorId}/appointments")
    @PreAuthorize("hasRole('ADMINISTRATOR') "
            + "or (hasRole('PATIENT') and #patientId == authentication.principal.id)")
    public ResponseEntity<AppointmentResponse> createAppointment(
            @PathVariable Integer patientId,
            @PathVariable Integer doctorId,
            @Valid @RequestBody PatientAppointmentRequest request) {

        AppointmentRequest appointmentRequest = new AppointmentRequest();
        appointmentRequest.setPatientId(patientId);
        appointmentRequest.setDoctorId(doctorId);
        appointmentRequest.setDate(request.getDate());
        appointmentRequest.setTime(request.getTime());
        appointmentRequest.setDoctorScheduleId(request.getDoctorScheduleId());
        appointmentRequest.setStatus(DEFAULT_APPOINTMENT_STATUS);
        appointmentRequest.setReason(request.getReason());
        appointmentRequest.setModifiedBy(patientId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(appointmentService.create(appointmentRequest));
    }

    @DeleteMapping("/{patientId}/appointments/{appointmentId}")
    @PreAuthorize("hasRole('ADMINISTRATOR') "
            + "or (hasRole('PATIENT') and #patientId == authentication.principal.id)")
    public ResponseEntity<Void> deleteAppointment(
            @PathVariable Integer patientId,
            @PathVariable Integer appointmentId) {

        boolean appointmentBelongsToPatient = appointmentService.getByPatient(patientId)
                .stream()
                .anyMatch(appointment -> appointment.getId().equals(appointmentId));

        if (!appointmentBelongsToPatient) {
            throw new RuntimeException("La cita no pertenece al paciente indicado");
        }

        appointmentService.delete(appointmentId);
        return ResponseEntity.noContent().build();
    }
}
