package com.OdontoGate.ArtefactoOdontoGate.exception;

/**
 * Define el contrato publico de ReceiptExceptions.
 */
public class ReceiptExceptions {

    private ReceiptExceptions() {}

    /**
     * Ejecuta la operacion publica miembro.
     */
    public static class PaymentNotFoundException extends RuntimeException {
        /**
         * Ejecuta la operacion publica PaymentNotFoundException.
         */
        public PaymentNotFoundException(Integer id) {
            super("Pago no encontrado con id: " + id);
        }
    }

    /**
     * Ejecuta la operacion publica miembro.
     */
    public static class PaymentNotPaidException extends RuntimeException {
        /**
         * Ejecuta la operacion publica PaymentNotPaidException.
         */
        public PaymentNotPaidException() {
            super("No se puede generar comprobante de un pago pendiente");
        }
    }

    /**
     * Ejecuta la operacion publica miembro.
     */
    public static class ReceiptAlreadyExistsException extends RuntimeException {
        /**
         * Ejecuta la operacion publica ReceiptAlreadyExistsException.
         */
        public ReceiptAlreadyExistsException() {
            super("Este pago ya tiene un comprobante");
        }
    }

}
