package com.OdontoGate.ArtefactoOdontoGate.exception;

/**
 * Define el contrato publico de MedicalRecordExceptions.
 */
public class MedicalRecordExceptions {

    private MedicalRecordExceptions(){}

    /**
     * Ejecuta la operacion publica miembro.
     */
    public static class NotFoundException extends RuntimeException {
        /**
         * Ejecuta la operacion publica NotFoundException.
         */
        public NotFoundException(Integer id) {
            super("Historia clínica no encontrada con id: " + id);
        }
    }

    /**
     * Ejecuta la operacion publica miembro.
     */
    public static class ValidationException extends RuntimeException {
        /**
         * Ejecuta la operacion publica ValidationException.
         */
        public ValidationException(String message) {
            super(message);
        }
    }
}