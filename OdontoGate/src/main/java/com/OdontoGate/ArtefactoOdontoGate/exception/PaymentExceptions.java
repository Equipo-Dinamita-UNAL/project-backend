package com.OdontoGate.ArtefactoOdontoGate.exception;

/**
 * Define el contrato publico de PaymentExceptions.
 */
public class PaymentExceptions {

    private PaymentExceptions() {}

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
    public static class AppointmentNotFoundException extends RuntimeException {
        /**
         * Ejecuta la operacion publica AppointmentNotFoundException.
         */
        public AppointmentNotFoundException(Integer id) {
            super("Cita no encontrada con id: " + id);
        }
    }

    /**
     * Ejecuta la operacion publica miembro.
     */
    public static class PaymentAlreadyExistsException extends RuntimeException {
        /**
         * Ejecuta la operacion publica PaymentAlreadyExistsException.
         */
        public PaymentAlreadyExistsException() {
            super("Esta cita ya tiene un pago");
        }
    }

    /**
     * Ejecuta la operacion publica miembro.
     */
    public static class InvalidAmountException extends RuntimeException {
        /**
         * Ejecuta la operacion publica InvalidAmountException.
         */
        public InvalidAmountException() {
            super("El monto no puede ser negativo");
        }
    }

}
