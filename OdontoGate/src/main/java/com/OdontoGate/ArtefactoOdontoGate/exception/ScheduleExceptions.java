package com.OdontoGate.ArtefactoOdontoGate.exception;

public class ScheduleExceptions {

    private ScheduleExceptions() {}

        public static class InvalidScheduleTimeException extends RuntimeException {
                public InvalidScheduleTimeException() {
            super("La hora de inicio debe ser anterior a la hora de fin");
        }
    }

}
