package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.ReceiptRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.ReceiptResponse;
import com.OdontoGate.ArtefactoOdontoGate.service.ReceiptService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {
    private final ReceiptService receiptService;

        public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    // Generar comprobante - POST
        @PostMapping
    public ResponseEntity<ReceiptResponse> createReceipt(
            @Valid @RequestBody ReceiptRequest request) {
        return ResponseEntity.status(201).body(receiptService.createReceipt(request));
    }

    // Obtener comprobante por pago - GET
        @GetMapping("/pago/{paymentId}")
    public ResponseEntity<ReceiptResponse> getReceiptByPayment(@PathVariable Integer paymentId) {
        return ResponseEntity.ok(receiptService.getReceiptByPayment(paymentId));
    }

    // Obtener comprobante por id - GET
        @GetMapping("/{id}")
    public ResponseEntity<ReceiptResponse> getReceiptById(@PathVariable Integer id) {
        return ResponseEntity.ok(receiptService.getReceiptById(id));
    }
}
