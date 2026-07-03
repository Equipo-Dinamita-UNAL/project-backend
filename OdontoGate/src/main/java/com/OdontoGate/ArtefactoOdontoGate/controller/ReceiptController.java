package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.ReceiptRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.ReceiptResponse;
import com.OdontoGate.ArtefactoOdontoGate.service.ReceiptService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {
    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping
    public ResponseEntity<ReceiptResponse> createReceipt(@Valid @RequestBody ReceiptRequest request) {
        return ResponseEntity.status(201).body(receiptService.createReceipt(request));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'PATIENT', 'DOCTOR')")
    @GetMapping("/pago/{paymentId}")
    public ResponseEntity<ReceiptResponse> getReceiptByPayment(@PathVariable Integer paymentId) {
        return ResponseEntity.ok(receiptService.getReceiptByPayment(paymentId));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'PATIENT', 'DOCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ReceiptResponse> getReceiptById(@PathVariable Integer id) {
        return ResponseEntity.ok(receiptService.getReceiptById(id));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadReceiptPdf(@PathVariable Integer id) {
        try {
            // El controlador solo le pide los bytes terminados a su servicio asignado
            byte[] pdfBytes = receiptService.getReceiptPdfBytes(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "recibo-" + id + ".pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
