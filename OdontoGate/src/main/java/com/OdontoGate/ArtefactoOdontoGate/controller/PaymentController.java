package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.PaymentRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.PaymentResponse;
import com.OdontoGate.ArtefactoOdontoGate.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Define el contrato publico de PaymentController.
 */
@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;

    /**
     * Ejecuta la operacion publica PaymentController.
     */
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Crear un pago -POST
    /**
     * Ejecuta la operacion publica createPayment.
     */
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest request) {
        return ResponseEntity.status(201).body(paymentService.createPayment(request));
    }

    // Ver pagos por paciente (navegando por appointment) - GET
    /**
     * Ejecuta la operacion publica getPaymentPatient.
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentPatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(paymentService.getPaymentPatient(patientId));
    }

    // Ver pago por la cita
    /**
     * Ejecuta la operacion publica getPaymentByAppointment.
     */
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<PaymentResponse> getPaymentByAppointment(@PathVariable Integer appointmentId) {
        return ResponseEntity.ok(paymentService.getPaymentByAppointment(appointmentId));
    }

    // Ver todos los pagos (solo admin) - GET
    /**
     * Ejecuta la operacion publica getAllPayments.
     */
    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    // Ver pago por id - GET
    /**
     * Ejecuta la operacion publica getPaymentById.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Integer id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    // Actualizar estado - PUT
    /**
     * Ejecuta la operacion publica updateStatus.
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<PaymentResponse> updateStatus(@PathVariable Integer id, @RequestBody String status) {
        return ResponseEntity.ok(paymentService.updateStatus(id, status));
    }

}
