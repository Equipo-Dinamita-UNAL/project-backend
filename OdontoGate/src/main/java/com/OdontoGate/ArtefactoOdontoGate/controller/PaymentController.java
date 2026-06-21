package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.PaymentRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.PaymentResponse;
import com.OdontoGate.ArtefactoOdontoGate.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;

        public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Crear un pago -POST
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'PATIENT')")
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(201).body(paymentService.createPayment(request));
    }

    // Ver pagos por paciente (navegando por appointment) - GET
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'PATIENT', 'DOCTOR')")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentPatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(paymentService.getPaymentPatient(patientId));
    }

    // Ver pago por la cita
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'PATIENT', 'DOCTOR')")
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<PaymentResponse> getPaymentByAppointment(@PathVariable Integer appointmentId) {
        return ResponseEntity.ok(paymentService.getPaymentByAppointment(appointmentId));
    }

    // Ver todos los pagos (solo admin) - GET
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    // Ver pago por id - GET
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'PATIENT', 'DOCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Integer id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    // Consultar cartera por rango de fechas - GET
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/cartera")
    public ResponseEntity<List<PaymentResponse>> getCarteraByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        return ResponseEntity.ok(paymentService.getPaymentsByDateRange(start, end));
    }

    // Actualizar estado - PUT
        @PreAuthorize("hasRole('ADMINISTRATOR')")
        @PutMapping("/{id}/estado")
    public ResponseEntity<PaymentResponse> updateStatus(@PathVariable Integer id, @RequestBody String status) {
        return ResponseEntity.ok(paymentService.updateStatus(id, status));
    }

}
