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
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/virtual")
    public ResponseEntity<PaymentResponse> createVirtualPayment(
            @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(201).body(paymentService.createVirtualPayment(request));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/presencial")
    public ResponseEntity<PaymentResponse> createPresentialPayment(
            @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(201).body(paymentService.createPresentialPayment(request));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'PATIENT', 'DOCTOR')")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentPatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(paymentService.getPaymentPatient(patientId));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'PATIENT', 'DOCTOR')")
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<PaymentResponse> getPaymentByAppointment(@PathVariable Integer appointmentId) {
        return ResponseEntity.ok(paymentService.getPaymentByAppointment(appointmentId));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'PATIENT', 'DOCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Integer id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/cartera")
    public ResponseEntity<List<PaymentResponse>> getCarteraByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        return ResponseEntity.ok(paymentService.getPaymentsByDateRange(start, end));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/{id}/estado")
    public ResponseEntity<PaymentResponse> updateStatus(@PathVariable Integer id, @RequestBody String status) {
        return ResponseEntity.ok(paymentService.updateStatus(id, status, null));
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> receiveWebhook(@RequestBody Map<String, Object> body) {
        paymentService.processWebhook(body);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/{id}/retry")
    public ResponseEntity<PaymentResponse> retryPayment(@PathVariable Integer id) {
        return ResponseEntity.ok(paymentService.retryPayment(id));
    }

}
