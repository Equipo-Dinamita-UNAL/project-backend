package com.OdontoGate.ArtefactoOdontoGate.exception;

public class AppointmentExceptions {

    private AppointmentExceptions() {}

        public static class InvalidDateException extends RuntimeException {
                public InvalidDateException() {
            super("La cita debe agendarse en una fecha futura");
        }
    }

        public static class ScheduleNotFoundException extends RuntimeException {
                public ScheduleNotFoundException(Integer id) {
            super("Horario no encontrado con id: " + id);
        }
    }

        public static class ScheduleNotAvailableException extends RuntimeException {
                public ScheduleNotAvailableException() {
            super("El horario seleccionado no está disponible");
        }
    }

        public static class DoctorConflictException extends RuntimeException {
                public DoctorConflictException() {
            super("El doctor ya tiene una cita agendada en esa fecha y hora");
        }
    }

}
