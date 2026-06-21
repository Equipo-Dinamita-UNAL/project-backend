package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.AppointmentRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.AppointmentResponse;
import com.OdontoGate.ArtefactoOdontoGate.exception.AppointmentExceptions;
import com.OdontoGate.ArtefactoOdontoGate.model.Appointment;
import com.OdontoGate.ArtefactoOdontoGate.model.Doctor;
import com.OdontoGate.ArtefactoOdontoGate.model.Patient;
import com.OdontoGate.ArtefactoOdontoGate.model.Schedule;
import com.OdontoGate.ArtefactoOdontoGate.model.User;
import com.OdontoGate.ArtefactoOdontoGate.model.Treatment;
import com.OdontoGate.ArtefactoOdontoGate.repository.AppointmentRepository;
import com.OdontoGate.ArtefactoOdontoGate.dto.request.AppointmentUpdateRequest;
import com.OdontoGate.ArtefactoOdontoGate.repository.PatientRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.DoctorRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.ScheduleRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.TreatmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ScheduleRepository scheduleRepository;

    // 1. REPOSITORIO DE TRATAMIENTOS INYECTADO AUTOMÁTICAMENTE POR LOMBOK 🚀
    private final TreatmentRepository treatmentRepository;

    // Crear cita
    public AppointmentResponse create(AppointmentRequest request) {
        Appointment appointment = new Appointment();
        appointment.setDate(request.getDate());
        appointment.setTime(request.getTime());
        appointment.setStatus(request.getStatus());
        appointment.setReason(request.getReason());

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        Schedule schedule = new Schedule();
        schedule.setId(request.getDoctorScheduleId());
        appointment.setDoctorSchedule(schedule);

        User modifiedBy = new User();
        modifiedBy.setId(request.getModifiedBy());
        appointment.setModifiedBy(modifiedBy);

        validarCita(
                request.getDate(),
                request.getTime(),
                request.getDoctorId(),
                request.getDoctorScheduleId(),
                null
        );

        appointmentRepository.save(appointment);
        Appointment saved = appointmentRepository.findById(appointment.getId())
                .orElseThrow(() -> new RuntimeException("Error al guardar la cita"));
        return toResponse(saved);
    }

    // Obtener todas las citas
    public List<AppointmentResponse> getAll() {
        return appointmentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Obtener citas de un paciente
    public List<AppointmentResponse> getByPatient(Integer patientId) {
        return appointmentRepository.findByPatientId(patientId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Obtener citas de un doctor
    public List<AppointmentResponse> getByDoctor(Integer doctorId) {
        return appointmentRepository.findByDoctorId(doctorId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Eliminar cita
    public void delete(Integer id) {
        appointmentRepository.deleteById(id);
    }

    // Modificar cita
    public AppointmentResponse update(Integer id, AppointmentUpdateRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (request.getDate() != null) { appointment.setDate(request.getDate()); }
        if (request.getTime() != null) { appointment.setTime(request.getTime()); }
        if (request.getReason() != null) { appointment.setReason(request.getReason()); }

        if (request.getDoctorScheduleId() != null) {
            Schedule schedule = new Schedule();
            schedule.setId(request.getDoctorScheduleId());
            appointment.setDoctorSchedule(schedule);
        }

        if (request.getModifiedBy() != null) {
            User modifiedBy = new User();
            modifiedBy.setId(request.getModifiedBy());
            appointment.setModifiedBy(modifiedBy);
        }

        LocalDate fechaFinal = request.getDate() != null ? request.getDate() : appointment.getDate();
        LocalTime horaFinal = request.getTime() != null ? request.getTime() : appointment.getTime();
        Integer scheduleId = request.getDoctorScheduleId() != null
                ? request.getDoctorScheduleId()
                : appointment.getDoctorSchedule().getId();

        validarCita(fechaFinal, horaFinal, appointment.getDoctor().getId(), scheduleId, id);

        appointmentRepository.save(appointment);
        Appointment saved = appointmentRepository.findById(appointment.getId())
                .orElseThrow(() -> new RuntimeException("Error al guardar la cita"));
        return toResponse(saved);
    }

    // Cancelar cita
    public AppointmentResponse cancel(Integer id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        appointment.setStatus("cancelada");

        appointmentRepository.save(appointment);
        Appointment saved = appointmentRepository.findById(appointment.getId())
                .orElseThrow(() -> new RuntimeException("Error al cancelar la cita"));
        return toResponse(saved);
    }

    private void validarCita(LocalDate date, LocalTime time, Integer doctorId,
                             Integer scheduleId, Integer excludeId) {

        // 1. Fecha futura
        if (!date.isAfter(LocalDate.now())) {
            throw new AppointmentExceptions.InvalidDateException();
        }

        // 2. Horario disponible
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppointmentExceptions.ScheduleNotFoundException(scheduleId));

        if (Boolean.FALSE.equals(schedule.getIsAvailable())) {
            throw new AppointmentExceptions.ScheduleNotAvailableException();
        }

        // 3. Sin conflicto de horario con el doctor
        List<Appointment> conflictos = appointmentRepository
                .findByDoctorIdAndDateAndTime(doctorId, date, time);

        boolean hayConflicto = conflictos.stream()
                .anyMatch(a -> !a.getId().equals(excludeId));

        if (hayConflicto) {
            throw new AppointmentExceptions.DoctorConflictException();
        }
    }

    // 2. CÁLCULO DINÁMICO DESDE LA BASE DE DATOS (MÉTODO MEJORADO) 🚀
    private Double calculatePriceByReason(String reason) {
        if (reason == null) {
            return 70000.00; // Tarifa básica por defecto si la cita no tiene motivo
        }

        // Busca el nombre en la tabla 'treatment'. Si existe extrae su precio, si no, usa la tarifa base.
        return treatmentRepository.findByName(reason.trim())
                .map(Treatment::getPrice)
                .orElse(70000.00);
    }

    // Convertir entidad a Response
    private AppointmentResponse toResponse(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setDate(appointment.getDate());
        response.setTime(appointment.getTime());
        response.setStatus(appointment.getStatus());
        response.setReason(appointment.getReason());
        response.setPatientName(
                appointment.getPatient().getName() + " " +
                        appointment.getPatient().getLastname()
        );
        response.setDoctorName(
                appointment.getDoctor().getName() + " " +
                        appointment.getDoctor().getLastname()
        );

        // ASIGNACIÓN DE PRECIO AUTOMÁTICO DINÁMICO ✅
        response.setPrice(calculatePriceByReason(appointment.getReason()));

        return response;
    }
}