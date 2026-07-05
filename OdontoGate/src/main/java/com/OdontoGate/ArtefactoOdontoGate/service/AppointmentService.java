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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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
    private final TreatmentRepository treatmentRepository;

    public AppointmentResponse create(AppointmentRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // 🛑 NUEVA VALIDACIÓN: Paciente inactivo
        if (!Boolean.TRUE.equals(patient.getActive())) {
            throw new RuntimeException("No se puede agendar la cita: el paciente se encuentra inactivo.");
        }

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));

        Schedule schedule = scheduleRepository.findById(request.getDoctorScheduleId())
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        User modifiedBy = new User();
        modifiedBy.setId(request.getModifiedBy());

        validarCita(
                request.getDate(),
                request.getTime(),
                request.getDoctorId(),
                schedule,
                null
        );

        Appointment appointment = new Appointment();
        appointment.setDate(request.getDate());
        appointment.setTime(request.getTime());
        appointment.setStatus(request.getStatus());
        appointment.setReason(request.getReason());
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDoctorSchedule(schedule);
        appointment.setModifiedBy(modifiedBy);

        appointmentRepository.save(appointment);

        Appointment saved = appointmentRepository.findById(appointment.getId())
                .orElseThrow(() -> new RuntimeException("Error al guardar la cita"));
        return toResponse(saved);
    }

    public AppointmentResponse create(AppointmentRequest request,
                                      Integer requestingUserId,
                                      boolean patientRequester) {
        validateRequestedPatient(request.getPatientId(), requestingUserId, patientRequester);
        return create(request);
    }

    public List<AppointmentResponse> getAll() {
        return appointmentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<AppointmentResponse> getByPatient(Integer patientId) {
        return appointmentRepository.findByPatientId(patientId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<AppointmentResponse> getByPatient(Integer patientId,
                                                  Integer requestingUserId,
                                                  boolean patientRequester) {
        validateRequestedPatient(patientId, requestingUserId, patientRequester);
        return getByPatient(patientId);
    }

    public List<AppointmentResponse> getByDoctor(Integer doctorId) {
        return appointmentRepository.findByDoctorId(doctorId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<AppointmentResponse> getByDoctor(Integer doctorId,
                                                 Integer requestingUserId,
                                                 boolean doctorRequester) {
        if (doctorRequester && !requestingUserId.equals(doctorId)) {
            throwForbidden();
        }
        return getByDoctor(doctorId);
    }

    public void delete(Integer id) {
        appointmentRepository.deleteById(id);
    }

    public void delete(Integer id, Integer requestingUserId, boolean patientRequester) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        validateAppointmentOwner(appointment, requestingUserId, patientRequester);
        appointmentRepository.deleteById(id);
    }

    public AppointmentResponse update(Integer id, AppointmentUpdateRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        return updateAppointment(id, request, appointment);
    }

    public AppointmentResponse update(Integer id,
                                      AppointmentUpdateRequest request,
                                      Integer requestingUserId,
                                      boolean patientRequester) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        validateAppointmentOwner(appointment, requestingUserId, patientRequester);
        return updateAppointment(id, request, appointment);
    }

    private AppointmentResponse updateAppointment(
            Integer id,
            AppointmentUpdateRequest request,
            Appointment appointment) {
        if (request.getDate() != null) {
            appointment.setDate(request.getDate());
        }
        if (request.getTime() != null) {
            appointment.setTime(request.getTime());
        }
        if (request.getReason() != null) {
            appointment.setReason(request.getReason());
        }

        if (request.getDoctorScheduleId() != null) {
            Schedule newSchedule = scheduleRepository.findById(request.getDoctorScheduleId())
                    .orElseThrow(() -> new RuntimeException("Horario no encontrado"));
            appointment.setDoctorSchedule(newSchedule);
        }

        if (request.getModifiedBy() != null) {
            User modifiedBy = new User();
            modifiedBy.setId(request.getModifiedBy());
            appointment.setModifiedBy(modifiedBy);
        }

        LocalDate fechaFinal = request.getDate() != null ? request.getDate() : appointment.getDate();
        LocalTime horaFinal = request.getTime() != null ? request.getTime() : appointment.getTime();
        Schedule scheduleFinal = appointment.getDoctorSchedule();

        validarCita(fechaFinal, horaFinal, appointment.getDoctor().getId(), scheduleFinal, id);

        appointmentRepository.save(appointment);
        Appointment saved = appointmentRepository.findById(appointment.getId())
                .orElseThrow(() -> new RuntimeException("Error al guardar la cita"));
        return toResponse(saved);
    }

    public AppointmentResponse cancel(Integer id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        // ✅ Ya no necesitamos liberar el schedule
        appointment.setStatus("cancelada");
        appointmentRepository.save(appointment);

        Appointment saved = appointmentRepository.findById(appointment.getId())
                .orElseThrow(() -> new RuntimeException("Error al cancelar la cita"));
        return toResponse(saved);
    }

    public AppointmentResponse cancel(Integer id, Integer requestingUserId, boolean patientRequester) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        validateAppointmentOwner(appointment, requestingUserId, patientRequester);

        appointment.setStatus("cancelada");
        appointmentRepository.save(appointment);

        Appointment saved = appointmentRepository.findById(appointment.getId())
                .orElseThrow(() -> new RuntimeException("Error al cancelar la cita"));
        return toResponse(saved);
    }

    private void validateRequestedPatient(
            Integer patientId,
            Integer requestingUserId,
            boolean patientRequester) {
        if (patientRequester && !requestingUserId.equals(patientId)) {
            throwForbidden();
        }
    }

    private void validateAppointmentOwner(
            Appointment appointment,
            Integer requestingUserId,
            boolean patientRequester) {
        if (patientRequester && !appointment.getPatient().getId().equals(requestingUserId)) {
            throwForbidden();
        }
    }

    private void throwForbidden() {
        throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "No tienes permiso para acceder a esta cita");
    }

    private void validarCita(LocalDate date, LocalTime time, Integer doctorId,
                             Schedule schedule, Integer excludeId) {

        // 1. Fecha futura
        if (!date.isAfter(LocalDate.now())) {
            throw new AppointmentExceptions.InvalidDateException();
        }

        // 2. El schedule no debe estar bloqueado manualmente
        if (Boolean.FALSE.equals(schedule.getIsAvailable())) {
            throw new AppointmentExceptions.ScheduleNotAvailableException();
        }

        // 3. El día de la semana de la fecha debe coincidir con el weekday del schedule
        String diaSemana = date.getDayOfWeek().name(); // "MONDAY", "TUESDAY", etc.
        if (!diaSemana.equals(schedule.getWeekday())) {
            throw new RuntimeException(
                    "La fecha seleccionada (" + diaSemana + ") no corresponde al dia del horario (" + schedule.getWeekday() + ")"
            );
        }

        // 4. La hora debe estar dentro del rango del schedule
        if (time.isBefore(schedule.getStartTime()) || !time.isBefore(schedule.getEndTime())) {
            throw new RuntimeException(
                    "La hora " + time + " esta fuera del rango del horario (" +
                            schedule.getStartTime() + " - " + schedule.getEndTime() + ")"
            );
        }

        // 5. Sin conflicto: no puede haber otra cita activa para ese doctor en esa fecha y hora
        List<Appointment> conflictos = appointmentRepository
                .findByDoctorIdAndDateAndTime(doctorId, date, time);

        boolean hayConflicto = conflictos.stream()
                .filter(a -> !"cancelada".equalsIgnoreCase(a.getStatus()))
                .anyMatch(a -> !a.getId().equals(excludeId));

        if (hayConflicto) {
            throw new AppointmentExceptions.DoctorConflictException();
        }
    }

    private Double calculatePriceByReason(String reason) {
        if (reason == null) {
            return 70000.00;
        }
        return treatmentRepository.findByName(reason.trim())
                .map(Treatment::getPrice)
                .orElse(70000.00);
    }

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
        response.setPrice(calculatePriceByReason(appointment.getReason()));
        return response;
    }
}
