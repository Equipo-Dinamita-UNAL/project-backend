package com.OdontoGate.ArtefactoOdontoGate.exception;

public class PaymentExceptions {

    private PaymentExceptions() {}

        public static class PaymentNotFoundException extends RuntimeException {
                public PaymentNotFoundException(Integer id) {
            super("Pago no encontrado con id: " + id);
        }
    }

        public static class AppointmentNotFoundException extends RuntimeException {
                public AppointmentNotFoundException(Integer id) {
            super("Cita no encontrada con id: " + id);
        }
    }

        public static class PaymentAlreadyExistsException extends RuntimeException {
                public PaymentAlreadyExistsException() {
            super("Esta cita ya tiene un pago");
        }
    }

        public static class InvalidAmountException extends RuntimeException {
                public InvalidAmountException() {
            super("El monto no puede ser negativo");
        }
    }

}
