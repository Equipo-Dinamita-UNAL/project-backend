package com.OdontoGate.ArtefactoOdontoGate.exception;

public class MercadoPagoIntegrationException extends RuntimeException {

    // Constructor para enviar solo un mensaje
    public MercadoPagoIntegrationException(String message) {
        super(message);
    }

    // Constructor para enviar el mensaje y el error original (causa)
    public MercadoPagoIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }

}
