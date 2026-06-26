package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.PaymentRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.PaymentResponse;
import com.OdontoGate.ArtefactoOdontoGate.exception.PaymentExceptions;
import com.OdontoGate.ArtefactoOdontoGate.model.Appointment;
import com.OdontoGate.ArtefactoOdontoGate.model.Payment;
import com.OdontoGate.ArtefactoOdontoGate.model.Treatment;
import com.OdontoGate.ArtefactoOdontoGate.repository.AppointmentRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.PaymentRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.TreatmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;

    // 👈 2. Inyectamos el TreatmentRepository para verificar precios reales en DB
    private final TreatmentRepository treatmentRepository;

    // Crear pago
    public PaymentResponse createPayment(PaymentRequest request) {

        // 1. Verificar que la cita existe
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new PaymentExceptions.AppointmentNotFoundException(request.getAppointmentId()));

        // 2. Verificar que la cita no tenga un pago previo asignado
        paymentRepository.findByAppointmentId(request.getAppointmentId())
                .ifPresent(p -> { throw new PaymentExceptions.PaymentAlreadyExistsException(); });

        // 🚀 3. NUEVA VALIDACIÓN FINANCIERA AUTOMÁTICA Y SEGURA
        // Buscamos el precio real de la cita en la tabla 'treatment'. Si no existe, usamos la tarifa base ($70,000)
        Double precioEsperadoDouble = treatmentRepository.findByName(appointment.getReason().trim())
                .map(Treatment::getPrice)
                .orElse(70000.00);

        BigDecimal montoCorrectoBd = BigDecimal.valueOf(precioEsperadoDouble);

        // [OPCIONAL] Si de todas formas quieres verificar que el cliente mandó el monto exacto:
        if (request.getAmount() != null && request.getAmount().compareTo(montoCorrectoBd) != 0) {
            throw new PaymentExceptions.PriceMismatchException();
        }

        // 4. Crear el pago usando el valor real verificado en DB
        Payment payment = new Payment();
        payment.setAppointment(appointment);
        payment.setAmount(montoCorrectoBd); // 👈 Forzamos que se guarde el precio legal
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
    public PaymentResponse updateStatus(Integer id, String status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        payment.setStatus(status);

        // 💡 Regla de Negocio automática: Si el pago pasa a "PAGADO", se podría actualizar el estado de la cita a "atendida" o "pagada"
        if ("PAGADO".equalsIgnoreCase(status)) {
            Appointment app = payment.getAppointment();
            app.setStatus("pagada");
            appointmentRepository.save(app);
        }

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