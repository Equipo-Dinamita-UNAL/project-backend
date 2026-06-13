package com.OdontoGate.ArtefactoOdontoGate.service;


import com.OdontoGate.ArtefactoOdontoGate.dto.request.PaymentRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.PaymentResponse;
import com.OdontoGate.ArtefactoOdontoGate.model.Appointment;
import com.OdontoGate.ArtefactoOdontoGate.model.Payment;
import com.OdontoGate.ArtefactoOdontoGate.repository.AppointmentRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;

    // Crear pago
    public PaymentResponse createPayment(PaymentRequest request) {

        // 1. Verificar que la cita existe
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        // 2. Verificar que la cita no tiene pago
        paymentRepository.findByAppointmentId(request.getAppointmentId())
                .ifPresent(p -> { throw new RuntimeException("Esta cita ya tiene un pago"); });

        // 3. Crear el pago
        Payment payment = new Payment();
        payment.setAppointment(appointment);
        payment.setAmount(request.getAmount());
        payment.setMethod(request.getMethod());
        payment.setStatus("PENDIENTE");
        payment.setCreatedAt(LocalDateTime.now());

        return mapToResponse(paymentRepository.save(payment));
    }

    // Ver pagos por paciente
    public List<PaymentResponse> getPaymentPatient(Integer patientId) {
        return paymentRepository.findByAppointmentPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    //Ver pagos por cita
    public PaymentResponse getPaymentByAppointment(Integer appointmentId) {
        Payment payment = paymentRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado para esta cita"));
        return mapToResponse(payment);
    }

    // Ver todos los pagos
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Ver pago por id
    public PaymentResponse GetPaymentById(Integer id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        return mapToResponse(payment);
    }

    // Actualizar estado
    public PaymentResponse updateStatus(Integer id, String status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        payment.setStatus(status);
        return mapToResponse(paymentRepository.save(payment));
    }

    // Mapeo
    private PaymentResponse mapToResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setAmount(payment.getAmount());
        response.setMethod(payment.getMethod());
        response.setStatus(payment.getStatus());
        response.setGatewayReference(payment.getGatewayReference());
        response.setCreatedAt(payment.getCreatedAt());
        response.setPatientName(payment.getAppointment().getPatient().getName());
        response.setPatientLastname(payment.getAppointment().getPatient().getLastname());
        return response;
    }

}
