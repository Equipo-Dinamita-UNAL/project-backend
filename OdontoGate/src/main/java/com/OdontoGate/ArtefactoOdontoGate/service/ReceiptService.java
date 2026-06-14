package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.ReceiptRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.ReceiptResponse;
import com.OdontoGate.ArtefactoOdontoGate.exception.ReceiptExceptions;
import com.OdontoGate.ArtefactoOdontoGate.model.Payment;
import com.OdontoGate.ArtefactoOdontoGate.model.Receipt;
import com.OdontoGate.ArtefactoOdontoGate.repository.PaymentRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final PaymentRepository paymentRepository;

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

    // Mapeo
    private ReceiptResponse mapToResponse(Receipt receipt) {
        ReceiptResponse response = new ReceiptResponse();
        response.setId(receipt.getId());
        response.setReceiptNumber(receipt.getReceiptNumber());
        response.setType(receipt.getType());
        response.setIssueDate(receipt.getIssueDate());
        response.setPdfUrl(receipt.getPdfUrl());
        return response;
    }
}
