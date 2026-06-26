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
import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;
    private final ApplicationEventPublisher eventPublisher;

    // 👈 2. Inyectamos el TreatmentRepository para verificar precios reales en DB
    private final TreatmentRepository treatmentRepository;

    private final PaymentGateway paymentGateway;

    // Crear pago
    // ===== FLUJO VIRTUAL (el paciente paga con MercadoPago) =====
    @Transactional
    public PaymentResponse createVirtualPayment(PaymentRequest request) {

        Appointment appointment = findAppointmentOrThrow(request.getAppointmentId());
        validateNoExistingPayment(request.getAppointmentId());
        BigDecimal amount = calculateCorrectAmount(appointment, request.getAmount());

        Payment payment = buildPayment(appointment, amount, "MERCADO_PAGO", "PENDIENTE");
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

    // ===== FLUJO PRESENCIAL (el admin registra un pago ya recibido) =====
    @Transactional
    public PaymentResponse createPresentialPayment(PaymentRequest request) {

        Appointment appointment = findAppointmentOrThrow(request.getAppointmentId());
        validateNoExistingPayment(request.getAppointmentId());
        BigDecimal amount = calculateCorrectAmount(appointment, request.getAmount());

        Payment payment = buildPayment(appointment, amount, request.getMethod(), "PAGADO");
        Payment savedPayment = paymentRepository.save(payment);

        appointment.setStatus("pagada");
        appointmentRepository.save(appointment);

        eventPublisher.publishEvent(new PaymentApprovedEvent(savedPayment.getId()));

        return mapToResponse(savedPayment);
    }

    // ===== MÉTODOS PRIVADOS COMPARTIDOS =====

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
    @SuppressWarnings("unchecked")
    public void processWebhook(Map<String, Object> body) {
        String topic = (String) body.get("topic");
        String type = (String) body.get("type");

        String mpPaymentId = null;

        //REVISAR IFS Y SEPARAR EN METODOS PEQUEÑOS

        if ("merchant_order".equals(topic)) {
            // Ignoramos este topic, solo nos interesa "payment"
            System.out.println("⚠️ Notificación de merchant_order ignorada, esperando topic=payment");
            return;
        }

        // Formato 1: {"resource": "164578842435", "topic": "payment"}
        if ("payment".equals(topic) && body.get("resource") != null) {
            mpPaymentId = String.valueOf(body.get("resource"));
        }

        // Formato 2: {"data": {"id": 164578842435}, "type": "payment"}
        if (mpPaymentId == null && "payment".equals(type)) {
            Map<String, Object> data = (Map<String, Object>) body.get("data");
            if (data != null && data.get("id") != null) {
                mpPaymentId = String.valueOf(data.get("id"));
            }
        }

        if (mpPaymentId == null) {
            System.out.println("⚠️ No se pudo extraer el paymentId, saliendo");
            return;
        }

        System.out.println("✅ Procesando pago de MercadoPago con id: " + mpPaymentId);

        PaymentInfo info = paymentGateway.getPaymentInfo(mpPaymentId);

        Integer localPaymentId = Integer.parseInt(info.externalReference());

        Payment payment = paymentRepository.findById(localPaymentId)
                .orElseThrow(() -> new PaymentExceptions.PaymentNotFoundException(localPaymentId));

        if ("approved".equals(info.status())) {
            payment.setStatus("PAGADO");
            payment.setGatewayReference(mpPaymentId);
            paymentRepository.save(payment);

            Appointment appointment = payment.getAppointment();
            appointment.setStatus("pagada");
            appointmentRepository.save(appointment);

            eventPublisher.publishEvent(new PaymentApprovedEvent(payment.getId()));
        }
    }

    // Ver pagos por paciente
    public List<PaymentResponse> getPaymentPatient(Integer patientId) {
        return paymentRepository.findByAppointmentPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Ver pagos por cita
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
                Appointment app = payment.getAppointment();
                app.setStatus("pagada");
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