package com.OdontoGate.OdontoGate.service;

import com.OdontoGate.OdontoGate.dto.request.AppointmentRequest;
import com.OdontoGate.OdontoGate.dto.response.AppointmentResponse;
import com.OdontoGate.OdontoGate.model.*;
import com.OdontoGate.OdontoGate.repository.AppointmentRepository;
import com.OdontoGate.OdontoGate.dto.request.AppointmentUpdateRequest;
import com.OdontoGate.OdontoGate.repository.PatientRepository;
import com.OdontoGate.OdontoGate.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

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
                .collect(Collectors.toList());
    }

    // Obtener citas de un paciente
    public List<AppointmentResponse> getByPatient(Integer patientId) {
        return appointmentRepository.findByPatientId(patientId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Obtener citas de un doctor
    public List<AppointmentResponse> getByDoctor(Integer doctorId) {
        return appointmentRepository.findByDoctorId(doctorId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Eliminar cita
    public void delete(Integer id) {
        appointmentRepository.deleteById(id);
    }

    // Modificar cita
    public AppointmentResponse update(Integer id, AppointmentUpdateRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (request.getDate() != null) appointment.setDate(request.getDate());
        if (request.getTime() != null) appointment.setTime(request.getTime());
        if (request.getReason() != null) appointment.setReason(request.getReason());

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
        return response;
    }
}