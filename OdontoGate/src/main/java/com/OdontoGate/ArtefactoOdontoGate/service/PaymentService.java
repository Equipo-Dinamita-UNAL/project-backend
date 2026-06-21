package com.OdontoGate.ArtefactoOdontoGate.service;


import com.OdontoGate.ArtefactoOdontoGate.dto.request.PaymentRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.PaymentResponse;
import com.OdontoGate.ArtefactoOdontoGate.event.PaymentApprovedEvent;
import com.OdontoGate.ArtefactoOdontoGate.exception.PaymentExceptions;
import com.OdontoGate.ArtefactoOdontoGate.model.Appointment;
import com.OdontoGate.ArtefactoOdontoGate.model.Payment;
import com.OdontoGate.ArtefactoOdontoGate.repository.AppointmentRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;
    private final ApplicationEventPublisher eventPublisher;

    // Crear pago
        public PaymentResponse createPayment(PaymentRequest request) {

        // 1. Verificar que la cita existe
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new PaymentExceptions.AppointmentNotFoundException(request.getAppointmentId()));

        // 2. Verificar que la cita no tiene pago
        paymentRepository.findByAppointmentId(request.getAppointmentId())
                .ifPresent(p -> { throw new PaymentExceptions.PaymentAlreadyExistsException(); });

        // 3. Crear el pago
        Payment payment = new Payment();
        payment.setAppointment(appointment);
        payment.setAmount(BigDecimal.valueOf(70000));
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
                .toList();
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
                .toList();
    }

    // Ver pago por id
        public PaymentResponse getPaymentById(Integer id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        return mapToResponse(payment);
    }

    // Actualizar estado
        @Transactional
        public PaymentResponse updateStatus(Integer id, String status) {
            Payment payment = paymentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
            payment.setStatus(status);

            if ("PAGADO".equals(status)) {
                // ¡Gritas al aire que el pago se aprobó! No llamas a ReceiptService.
                eventPublisher.publishEvent(new PaymentApprovedEvent(payment.getId()));
            }

        return mapToResponse(paymentRepository.save(payment));
    }

    // Consultar cartera por rango de fechas (Para el Administrador)
    public List<PaymentResponse> getPaymentsByDateRange(LocalDateTime start, LocalDateTime end) {
        return paymentRepository.findByCreatedAtBetween(start, end)
                .stream()
                .map(this::mapToResponse)
                .toList();
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
