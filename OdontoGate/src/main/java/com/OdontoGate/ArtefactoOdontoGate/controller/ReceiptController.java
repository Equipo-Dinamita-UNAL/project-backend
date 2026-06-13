package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.ReceiptRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.ReceiptResponse;
import com.OdontoGate.ArtefactoOdontoGate.service.ReceiptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {
    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    // Generar comprobante - POST
    @PostMapping
    public ResponseEntity<ReceiptResponse> createReceipt(@RequestBody ReceiptRequest request) {
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
