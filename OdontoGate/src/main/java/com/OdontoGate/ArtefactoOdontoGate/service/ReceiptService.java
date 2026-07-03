package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.ReceiptRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.ReceiptResponse;
import com.OdontoGate.ArtefactoOdontoGate.exception.ReceiptExceptions;
import com.OdontoGate.ArtefactoOdontoGate.model.*;
import com.OdontoGate.ArtefactoOdontoGate.repository.PaymentRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final PaymentRepository paymentRepository;
    private final PdfService pdfService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ReceiptResponse createReceipt(ReceiptRequest request) {
        Payment payment = findPaymentOrThrow(request.getPaymentId());
        validatePaymentIsPaid(payment);
        validateNoExistingReceipt(request.getPaymentId());

        Receipt savedReceipt = buildAndSaveReceipt(payment, request.getType());

        return mapToResponse(savedReceipt);
    }

    private Payment findPaymentOrThrow(Integer paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ReceiptExceptions.PaymentNotFoundException(paymentId));
    }

    private void validatePaymentIsPaid(Payment payment) {
        boolean esIgual = "PAGADO".equals(payment.getStatus());

        if (!esIgual) {
            throw new ReceiptExceptions.PaymentNotPaidException();
        }
    }

    private void validateNoExistingReceipt(Integer paymentId) {
        receiptRepository.findByPaymentId(paymentId)
                .ifPresent(r -> { throw new ReceiptExceptions.ReceiptAlreadyExistsException(); });
    }

    private Receipt buildAndSaveReceipt(Payment payment, String type) {
        Receipt receipt = new Receipt();
        receipt.setPayment(payment);
        receipt.setType(type);
        receipt.setReceiptNumber("REC-" + System.currentTimeMillis());
        receipt.setIssueDate(LocalDateTime.now());
        receipt.setCreatedAt(LocalDateTime.now());
        return receiptRepository.save(receipt);
    }

        public ReceiptResponse getReceiptByPayment(Integer paymentId) {
        Receipt receipt = receiptRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Comprobante no encontrado"));
        return mapToResponse(receipt);
    }

        public ReceiptResponse getReceiptById(Integer id) {
        Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comprobante no encontrado"));
        return mapToResponse(receipt);
    }
    
    public byte[] getReceiptPdfBytes(Integer id) throws IOException {
        ReceiptResponse receiptInfo = this.getReceiptById(id);

        Map<String, Object> data = new HashMap<>();
        data.put("receipt", receiptInfo);

        return pdfService.generatePdf("receipt", data);
    }

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
        response.setGatewayReference(payment.getGatewayReference());
        return response;
    }
}
