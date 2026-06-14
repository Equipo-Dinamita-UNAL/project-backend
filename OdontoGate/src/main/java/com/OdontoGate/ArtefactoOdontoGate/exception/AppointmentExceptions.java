package com.OdontoGate.ArtefactoOdontoGate.exception;

/**
 * Define el contrato publico de AppointmentExceptions.
 */
public class AppointmentExceptions {

    private AppointmentExceptions() {}

    /**
     * Ejecuta la operacion publica miembro.
     */
    public static class InvalidDateException extends RuntimeException {
        /**
         * Ejecuta la operacion publica InvalidDateException.
         */
        public InvalidDateException() {
            super("La cita debe agendarse en una fecha futura");
        }
    }

    /**
     * Ejecuta la operacion publica miembro.
     */
    public static class ScheduleNotFoundException extends RuntimeException {
        /**
         * Ejecuta la operacion publica ScheduleNotFoundException.
         */
        public ScheduleNotFoundException(Integer id) {
            super("Horario no encontrado con id: " + id);
        }
    }

    /**
     * Ejecuta la operacion publica miembro.
     */
    public static class ScheduleNotAvailableException extends RuntimeException {
        /**
         * Ejecuta la operacion publica ScheduleNotAvailableException.
         */
        public ScheduleNotAvailableException() {
            super("El horario seleccionado no está disponible");
        }
    }

    /**
     * Ejecuta la operacion publica miembro.
     */
    public static class DoctorConflictException extends RuntimeException {
        /**
         * Ejecuta la operacion publica DoctorConflictException.
         */
        public DoctorConflictException() {
            super("El doctor ya tiene una cita agendada en esa fecha y hora");
        }
    }

}
