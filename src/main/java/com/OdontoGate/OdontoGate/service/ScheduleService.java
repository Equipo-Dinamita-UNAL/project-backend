package com.OdontoGate.OdontoGate.service;

import com.OdontoGate.OdontoGate.dto.request.ScheduleRequest;
import com.OdontoGate.OdontoGate.dto.response.ScheduleResponse;
import com.OdontoGate.OdontoGate.model.Doctor;
import com.OdontoGate.OdontoGate.model.Schedule;
import com.OdontoGate.OdontoGate.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    // Crear horario
    public ScheduleResponse create(ScheduleRequest request) {
        Schedule schedule = new Schedule();
        schedule.setWeekday(request.getWeekday());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setIsAvailable(request.getIsAvailable());

        // referencia al doctor por id
        Doctor doctor = new Doctor();
        doctor.setId(request.getDoctorId());
        schedule.setDoctor(doctor);

        Schedule saved = scheduleRepository.save(schedule);
        return toResponse(saved);
    }

    // Obtener todos los horarios
    public List<ScheduleResponse> getAll() {
        return scheduleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Obtener horarios de un doctor
    public List<ScheduleResponse> getByDoctor(Integer doctorId) {
        return scheduleRepository.findByDoctorId(doctorId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Eliminar horario
    public void delete(Integer id) {
        scheduleRepository.deleteById(id);
    }

    // Actualizar horario
    public ScheduleResponse update(Integer id, ScheduleRequest request) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        if (request.getWeekday() != null) schedule.setWeekday(request.getWeekday());
        if (request.getStartTime() != null) schedule.setStartTime(request.getStartTime());
        if (request.getEndTime() != null) schedule.setEndTime(request.getEndTime());
        if (request.getIsAvailable() != null) schedule.setIsAvailable(request.getIsAvailable());

        Schedule saved = scheduleRepository.save(schedule);
        return toResponse(saved);
    }

    // Marcar disponibilidad
    public ScheduleResponse setAvailable(Integer id, Boolean available) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        schedule.setIsAvailable(available);

        Schedule saved = scheduleRepository.save(schedule);
        return toResponse(saved);
    }

    // Convertir entidad a Response
    private ScheduleResponse toResponse(Schedule schedule) {
        ScheduleResponse response = new ScheduleResponse();
        response.setId(schedule.getId());
        response.setWeekday(schedule.getWeekday());
        response.setStartTime(schedule.getStartTime());
        response.setEndTime(schedule.getEndTime());
        response.setIsAvailable(schedule.getIsAvailable());
        response.setDoctorName(
                schedule.getDoctor().getName() + " " +
                        schedule.getDoctor().getLastname()
        );
        return response;
    }
}