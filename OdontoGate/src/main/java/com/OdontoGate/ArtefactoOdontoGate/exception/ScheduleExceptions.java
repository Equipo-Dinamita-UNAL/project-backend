package com.OdontoGate.ArtefactoOdontoGate.exception;

/**
 * Define el contrato publico de ScheduleExceptions.
 */
public class ScheduleExceptions {

    private ScheduleExceptions() {}

    /**
     * Ejecuta la operacion publica miembro.
     */
    public static class InvalidScheduleTimeException extends RuntimeException {
        /**
         * Ejecuta la operacion publica InvalidScheduleTimeException.
         */
        public InvalidScheduleTimeException() {
            super("La hora de inicio debe ser anterior a la hora de fin");
        }
    }

}
