package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.PaymentRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.PaymentResponse;
import com.OdontoGate.ArtefactoOdontoGate.event.PaymentApprovedEvent;
import com.OdontoGate.ArtefactoOdontoGate.exception.PaymentExceptions;
import com.OdontoGate.ArtefactoOdontoGate.gateway.PaymentGateway;
import com.OdontoGate.ArtefactoOdontoGate.gateway.PaymentInfo;
import com.OdontoGate.ArtefactoOdontoGate.model.Appointment;
import com.OdontoGate.ArtefactoOdontoGate.model.Payment;
import com.OdontoGate.ArtefactoOdontoGate.model.Treatment;
import com.OdontoGate.ArtefactoOdontoGate.repository.AppointmentRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.PaymentRepository;

import jakarta.transaction.Transactional;

import com.OdontoGate.ArtefactoOdontoGate.repository.TreatmentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final String STATUS_PAGADO = "PAGADO";
    private static final String STATUS_PENDIENTE = "PENDIENTE";

    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final TreatmentRepository treatmentRepository;
    private final PaymentGateway paymentGateway;

    @Transactional
    public PaymentResponse createVirtualPayment(PaymentRequest request) {

        Appointment appointment = findAppointmentOrThrow(request.getAppointmentId());
        validateNoExistingPayment(request.getAppointmentId());
        BigDecimal amount = calculateCorrectAmount(appointment, request.getAmount());

        Payment payment = buildPayment(appointment, amount, "MERCADO_PAGO", STATUS_PENDIENTE);
        Payment savedPayment = paymentRepository.save(payment);

        String checkoutUrl = paymentGateway.generateCheckoutUrl(
                savedPayment.getId().toString(),
                amount,
                "Cita odontológica"
        );

        PaymentResponse response = mapToResponse(savedPayment);
        response.setCheckoutUrl(checkoutUrl);
        return response;
    }


    @Transactional
    public PaymentResponse createPresentialPayment(PaymentRequest request) {

        Appointment appointment = findAppointmentOrThrow(request.getAppointmentId());
        validateNoExistingPayment(request.getAppointmentId());
        BigDecimal amount = calculateCorrectAmount(appointment, request.getAmount());

        Payment payment = buildPayment(appointment, amount, request.getMethod(), STATUS_PAGADO);
        Payment savedPayment = paymentRepository.save(payment);

        markAppointmentAsPaid(savedPayment);

        log.info("💰 Pago presencial #{} registrado y cita #{} marcada como pagada",
                savedPayment.getId(), appointment.getId());

        return mapToResponse(savedPayment);
    }

    private Appointment findAppointmentOrThrow(Integer appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new PaymentExceptions.AppointmentNotFoundException(appointmentId));
    }

    private void validateNoExistingPayment(Integer appointmentId) {
        paymentRepository.findByAppointmentId(appointmentId)
                .ifPresent(p -> { throw new PaymentExceptions.PaymentAlreadyExistsException(); });
    }

    private BigDecimal calculateCorrectAmount(Appointment appointment, BigDecimal requestedAmount) {
        Double expectedPrice = treatmentRepository.findByName(appointment.getReason().trim())
                .map(Treatment::getPrice)
                .orElse(70000.00);

        BigDecimal correctAmount = BigDecimal.valueOf(expectedPrice);

        if (requestedAmount != null && requestedAmount.compareTo(correctAmount) != 0) {
            throw new PaymentExceptions.PriceMismatchException();
        }
        return correctAmount;
    }

    private Payment buildPayment(Appointment appointment, BigDecimal amount, String method, String status) {
        Payment payment = new Payment();
        payment.setAppointment(appointment);
        payment.setAmount(amount);
        payment.setMethod(method);
        payment.setStatus(status);
        payment.setCreatedAt(LocalDateTime.now());
        return payment;
    }

    @Transactional
    public void processWebhook(Map<String, Object> body) {
        // Responsabilidad 1: Delegamos la extracción del ID
        String mpPaymentId = extractPaymentId(body);

        if (mpPaymentId == null) {
            return;
        }

        log.info("✅ Procesando pago de MercadoPago con id: {}", mpPaymentId);

        // Coordinación externa
        PaymentInfo info = paymentGateway.getPaymentInfo(mpPaymentId);
        Integer localPaymentId = Integer.parseInt(info.externalReference());

        // Responsabilidad 3: Delegamos la actualización del negocio si el pago fue aprobado
        if ("approved".equals(info.status())) {
            updateStatus(localPaymentId, STATUS_PAGADO, mpPaymentId);
        }
    }
    @SuppressWarnings("unchecked")
    private String extractPaymentId(Map<String, Object> body) {
        String topic = (String) body.get("topic");
        String type = (String) body.get("type");

        if ("merchant_order".equals(topic)) {
            log.info("⚠️ Notificación de merchant_order ignorada, esperando topic=payment");
            return null;
        }

        // Formato 1: {"resource": "164578842435", "topic": "payment"}
        if ("payment".equals(topic) && body.get("resource") != null) {
            return String.valueOf(body.get("resource"));
        }

        // Formato 2: {"data": {"id": 164578842435}, "type": "payment"}
        if ("payment".equals(type)) {
            Map<String, Object> data = (Map<String, Object>) body.get("data");
            if (data != null && data.get("id") != null) {
                return String.valueOf(data.get("id"));
            }
        }

        log.warn("⚠️ No se pudo extraer el paymentId del webhook recibido");
        return null;
    }

    @Transactional
    public PaymentResponse updateStatus(Integer id, String status, String gatewayReference) {
        Payment payment = paymentRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        if (status.equals(payment.getStatus())) {
            log.info("⚠️ Pago #{} ya estaba en estado {}, ignorando actualización duplicada", id, status);
            return mapToResponse(payment);
        }

        payment.setStatus(status);

        if (gatewayReference != null) {
            payment.setGatewayReference(gatewayReference);
        }

        paymentRepository.save(payment);

        log.info("🔄 Pago #{} actualizado a estado: {}", id, status);

        if (STATUS_PAGADO.equals(status)) {
            markAppointmentAsPaid(payment);
        }

        return mapToResponse(payment);
    }

    private void markAppointmentAsPaid(Payment payment) {
        Appointment appointment = payment.getAppointment();
        appointment.setStatus(STATUS_PAGADO);
        appointmentRepository.save(appointment);

        eventPublisher.publishEvent(new PaymentApprovedEvent(payment.getId()));

        log.info("📋 Cita #{} marcada como pagada, evento PaymentApprovedEvent publicado", appointment.getId());
    }

    public List<PaymentResponse> getPaymentPatient(Integer patientId) {
        return paymentRepository.findByAppointmentPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PaymentResponse getPaymentByAppointment(Integer appointmentId) {
        Payment payment = paymentRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado para esta cita"));
        return mapToResponse(payment);
    }

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PaymentResponse getPaymentById(Integer id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        return mapToResponse(payment);
    }

    public List<PaymentResponse> getPaymentsByDateRange(LocalDateTime start, LocalDateTime end) {
        return paymentRepository.findByCreatedAtBetween(start, end)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

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