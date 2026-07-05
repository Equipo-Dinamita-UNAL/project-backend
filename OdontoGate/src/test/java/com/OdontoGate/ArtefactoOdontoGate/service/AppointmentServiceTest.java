package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.AppointmentRequest;
import com.OdontoGate.ArtefactoOdontoGate.model.Appointment;
import com.OdontoGate.ArtefactoOdontoGate.model.Doctor;
import com.OdontoGate.ArtefactoOdontoGate.model.Patient;
import com.OdontoGate.ArtefactoOdontoGate.model.Schedule;
import com.OdontoGate.ArtefactoOdontoGate.repository.AppointmentRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.DoctorRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.PatientRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.ScheduleRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.TreatmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private TreatmentRepository treatmentRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    // Test 1: No se puede agendar en horario no disponible
    @Test
    void cuandoHorarioNoDisponible_debeLanzarExcepcion() {
        // Preparar datos
        AppointmentRequest request = new AppointmentRequest();
        request.setPatientId(1);
        request.setDoctorId(1);
        request.setDate(LocalDate.now().plusDays(1));
        request.setTime(LocalTime.of(9, 0));
        request.setDoctorScheduleId(1);
        request.setStatus("pendiente");
        request.setReason("Revisión");
        request.setModifiedBy(1);

        Patient patient = new Patient();
        patient.setId(1);
        patient.setActive(true);

        Doctor doctor = new Doctor();
        doctor.setId(1);

        Schedule schedule = new Schedule();
        schedule.setId(1);
        schedule.setIsAvailable(false); // horario NO disponible

        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor));
        when(scheduleRepository.findById(1)).thenReturn(Optional.of(schedule));

        // Verificar que lanza excepción
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> appointmentService.create(request));

        assertEquals("El horario seleccionado no está disponible", ex.getMessage());
    }

    // Test 2: No se pueden agendar dos citas al mismo tiempo con el mismo doctor
    @Test
    void cuandoYaExisteCitaMismoHorario_debeLanzarExcepcion() {
        AppointmentRequest request = new AppointmentRequest();
        request.setPatientId(1);
        request.setDoctorId(1);
        request.setDate(LocalDate.now().plusDays(1));
        request.setTime(LocalTime.of(9, 0));
        request.setDoctorScheduleId(1);
        request.setStatus("pendiente");
        request.setReason("Revisión");
        request.setModifiedBy(1);

        Patient patient = new Patient();
        patient.setId(1);
        patient.setActive(true);

        Doctor doctor = new Doctor();
        doctor.setId(1);

        Schedule schedule = new Schedule();
        schedule.setId(1);
        schedule.setIsAvailable(true);
        schedule.setWeekday(request.getDate().getDayOfWeek().name());
        schedule.setStartTime(LocalTime.of(8, 0));
        schedule.setEndTime(LocalTime.of(10, 0));

        // Cita existente con el mismo doctor, fecha y hora
        Appointment citaExistente = new Appointment();
        citaExistente.setId(99);

        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor));
        when(scheduleRepository.findById(1)).thenReturn(Optional.of(schedule));
        when(appointmentRepository.findByDoctorIdAndDateAndTime(1,
                request.getDate(), request.getTime()))
                .thenReturn(List.of(citaExistente));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> appointmentService.create(request));

        assertEquals("El doctor ya tiene una cita agendada en esa fecha y hora", ex.getMessage());
    }

    // Test 3: No se puede agendar con fecha pasada
    @Test
    void cuandoFechaEsPasada_debeLanzarExcepcion() {
        AppointmentRequest request = new AppointmentRequest();
        request.setPatientId(1);
        request.setDoctorId(1);
        request.setDate(LocalDate.now().minusDays(1)); // fecha pasada
        request.setTime(LocalTime.of(9, 0));
        request.setDoctorScheduleId(1);
        request.setStatus("pendiente");
        request.setReason("Revisión");
        request.setModifiedBy(1);

        Patient patient = new Patient();
        patient.setId(1);
        patient.setActive(true);

        Doctor doctor = new Doctor();
        doctor.setId(1);

        Schedule schedule = new Schedule();
        schedule.setId(1);
        schedule.setIsAvailable(true);

        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor));
        when(scheduleRepository.findById(1)).thenReturn(Optional.of(schedule));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> appointmentService.create(request));

        assertEquals("La cita debe agendarse en una fecha futura", ex.getMessage());
    }
}
