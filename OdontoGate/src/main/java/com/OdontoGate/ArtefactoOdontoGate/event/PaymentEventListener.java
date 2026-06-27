package com.OdontoGate.ArtefactoOdontoGate.event;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.ReceiptRequest;
import com.OdontoGate.ArtefactoOdontoGate.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final ReceiptService receiptService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentApproved(PaymentApprovedEvent event) {
        log.info("🔔 Evento recibido: Pago aprobado con ID {}. Generando recibo...", event.getPaymentId());
        try {
            ReceiptRequest receiptReq = new ReceiptRequest();
            receiptReq.setPaymentId(event.getPaymentId());
            receiptReq.setType("ELECTRONICO");

            receiptService.createReceipt(receiptReq);

        } catch (Exception e) {
            log.error("❌ Error al generar el recibo automático para el pago {}: {}", event.getPaymentId(), e.getMessage(), e);
        }
    }

}
