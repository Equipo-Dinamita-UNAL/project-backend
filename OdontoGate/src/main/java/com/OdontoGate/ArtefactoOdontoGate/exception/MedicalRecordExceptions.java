package com.OdontoGate.ArtefactoOdontoGate.exception;

public class MedicalRecordExceptions {

    private MedicalRecordExceptions() {}

    public static class NotFoundException extends RuntimeException {
        public NotFoundException(Integer id) {
            super("Historia clínica no encontrada con id: " + id);
        }
    }

    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }

    /**
     * Se lanza cuando un paciente intenta acceder a una historia clínica
     * que no le pertenece.
     */
    public static class PatientNotOwnerException extends RuntimeException {
        public PatientNotOwnerException() {
            super("No tienes permiso para acceder a esta historia clínica");
        }
    }
}