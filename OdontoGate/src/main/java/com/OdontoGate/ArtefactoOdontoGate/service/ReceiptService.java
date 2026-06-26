package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.ReceiptRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.ReceiptResponse;
import com.OdontoGate.ArtefactoOdontoGate.event.PaymentApprovedEvent;
import com.OdontoGate.ArtefactoOdontoGate.exception.ReceiptExceptions;
import com.OdontoGate.ArtefactoOdontoGate.model.*;
import com.OdontoGate.ArtefactoOdontoGate.repository.PaymentRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final PaymentRepository paymentRepository;
    private final PdfService pdfService;

    // Generar comprobante
        public ReceiptResponse createReceipt(ReceiptRequest request) {

        // 1. Verificar que el pago existe
        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new ReceiptExceptions.PaymentNotFoundException(request.getPaymentId()));

        // 2. Verificar que el pago está pagado
        if (!payment.getStatus().equals("PAGADO")) {
            throw new ReceiptExceptions.PaymentNotPaidException();
        }

        // 3. Verificar que no tiene comprobante
        receiptRepository.findByPaymentId(request.getPaymentId())
                .ifPresent(r -> { throw new ReceiptExceptions.ReceiptAlreadyExistsException(); });

        // 4. Crear comprobante
        Receipt receipt = new Receipt();
        receipt.setPayment(payment);
        receipt.setType(request.getType());
        receipt.setReceiptNumber("REC-" + System.currentTimeMillis());
        receipt.setIssueDate(LocalDateTime.now());
        receipt.setCreatedAt(LocalDateTime.now());

        return mapToResponse(receiptRepository.save(receipt));
    }

    // Obtener comprobante por pago
        public ReceiptResponse getReceiptByPayment(Integer paymentId) {
        Receipt receipt = receiptRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Comprobante no encontrado"));
        return mapToResponse(receipt);
    }

    // Obtener comprobante por id
        public ReceiptResponse getReceiptById(Integer id) {
        Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comprobante no encontrado"));
        return mapToResponse(receipt);
    }

    // Este metodo se dispara SOLO cuando alguien publica un PaymentApprovedEvent
    @EventListener
    public void handlePaymentApproved(PaymentApprovedEvent event) {
        ReceiptRequest receiptReq = new ReceiptRequest();
        receiptReq.setPaymentId(event.getPaymentId());
        receiptReq.setType("ELECTRONICO");
        this.createReceipt(receiptReq);
    }

    public byte[] getReceiptPdfBytes(Integer id) throws Exception {
        // 1. Buscamos la info usando tu lógica actual
        ReceiptResponse receiptInfo = this.getReceiptById(id);

        // 2. Metemos la info en un mapa genérico.
        // La clave "receipt" debe coincidir con como lo llamas en el HTML: th:text="${receipt.monto}"
        Map<String, Object> data = new HashMap<>();
        data.put("receipt", receiptInfo);

        // 3. Le pedimos al motor que genere el PDF usando la plantilla "recibo"
        return pdfService.generatePdf("receipt", data);
    }

    // Mapeo
    private ReceiptResponse mapToResponse(Receipt receipt) {
        Payment payment = receipt.getPayment();
        Appointment appointment = payment.getAppointment();
        Doctor doctor = appointment.getDoctor();
        Patient patient = appointment.getPatient();

        ReceiptResponse response = new ReceiptResponse();
        response.setId(receipt.getId());
        response.setReceiptNumber(receipt.getReceiptNumber());
        response.setType(receipt.getType());
        response.setIssueDate(receipt.getIssueDate());
        response.setPdfUrl(receipt.getPdfUrl());

        response.setTreatmentReason(appointment.getReason());
        response.setDoctorName(doctor.getName());
        response.setDoctorLastname(doctor.getLastname());
        response.setPatientName(patient.getName());
        response.setPatientLastname(patient.getLastname());
        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getMethod());
        response.setGatewayReference(payment.getGatewayReference()); // será null si fue presencial

        return response;
    }
}
