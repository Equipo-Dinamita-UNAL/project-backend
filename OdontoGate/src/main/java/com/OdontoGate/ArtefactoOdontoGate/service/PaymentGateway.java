package com.OdontoGate.ArtefactoOdontoGate.service;

import java.math.BigDecimal;

public interface PaymentGateway {
    String generateCheckoutUrl(String referenceId, BigDecimal amount, String title);
}
