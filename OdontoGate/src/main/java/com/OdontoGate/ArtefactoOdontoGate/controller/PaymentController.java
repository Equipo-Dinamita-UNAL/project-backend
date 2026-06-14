package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.PaymentRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.PaymentResponse;
import com.OdontoGate.ArtefactoOdontoGate.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Crear un pago -POST
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest request) {
        return ResponseEntity.status(201).body(paymentService.createPayment(request));
    }

    // Ver pagos por paciente (navegando por appointment) - GET
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentPatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(paymentService.getPaymentPatient(patientId));
    }

    // Ver pago por la cita
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<PaymentResponse> getPaymentByAppointment(@PathVariable Integer appointmentId) {
        return ResponseEntity.ok(paymentService.getPaymentByAppointment(appointmentId));
    }

    // Ver todos los pagos (solo admin) - GET
    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    // Ver pago por id - GET
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Integer id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    // Actualizar estado - PUT
    @PutMapping("/{id}/estado")
    public ResponseEntity<PaymentResponse> updateStatus(@PathVariable Integer id, @RequestBody String status) {
        return ResponseEntity.ok(paymentService.updateStatus(id, status));
    }

}
