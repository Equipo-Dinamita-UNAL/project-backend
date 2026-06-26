package com.OdontoGate.ArtefactoOdontoGate.gateway;

import java.math.BigDecimal;

public interface PaymentGateway {
    String generateCheckoutUrl(String referenceId, BigDecimal amount, String title);
    PaymentInfo getPaymentInfo(String mercadoPagoPaymentId);
}
