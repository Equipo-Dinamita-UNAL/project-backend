package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.PaymentRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.PaymentResponse;
import com.OdontoGate.ArtefactoOdontoGate.service.CurrentUserService;
import com.OdontoGate.ArtefactoOdontoGate.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final CurrentUserService currentUserService;

    public PaymentController(PaymentService paymentService,
                             CurrentUserService currentUserService) {
        this.paymentService = paymentService;
        this.currentUserService = currentUserService;
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/virtual")
    public ResponseEntity<PaymentResponse> createVirtualPayment(
            @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(201).body(paymentService.createVirtualPayment(
                request,
                currentUserService.getCurrentUserId(),
                currentUserService.isPatient()));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/presencial")
    public ResponseEntity<PaymentResponse> createPresentialPayment(
            @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(201).body(paymentService.createPresentialPayment(request));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') "
            + "or (hasRole('PATIENT') and #patientId == authentication.principal.id)")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentPatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(paymentService.getPaymentPatient(patientId));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('PATIENT')")
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<PaymentResponse> getPaymentByAppointment(@PathVariable Integer appointmentId) {
        return ResponseEntity.ok(paymentService.getPaymentByAppointment(
                appointmentId,
                currentUserService.getCurrentUserId(),
                currentUserService.isPatient()));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('PATIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Integer id) {
        return ResponseEntity.ok(paymentService.getPaymentById(
                id,
                currentUserService.getCurrentUserId(),
                currentUserService.isPatient()));
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
        return ResponseEntity.ok(paymentService.retryPayment(
                id,
                currentUserService.getCurrentUserId(),
                currentUserService.isPatient()));
    }

}
