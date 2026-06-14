package com.OdontoGate.ArtefactoOdontoGate.exception;

public class ReceiptExceptions {

    private ReceiptExceptions() {}

    public static class PaymentNotFoundException extends RuntimeException {
        public PaymentNotFoundException(Integer id) {
            super("Pago no encontrado con id: " + id);
        }
    }

    public static class PaymentNotPaidException extends RuntimeException {
        public PaymentNotPaidException() {
            super("No se puede generar comprobante de un pago pendiente");
        }
    }

    public static class ReceiptAlreadyExistsException extends RuntimeException {
        public ReceiptAlreadyExistsException() {
            super("Este pago ya tiene un comprobante");
        }
    }

}
