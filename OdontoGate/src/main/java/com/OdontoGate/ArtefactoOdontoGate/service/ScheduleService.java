package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.ScheduleRequest;
import com.OdontoGate.ArtefactoOdontoGate.dto.response.ScheduleResponse;
import com.OdontoGate.ArtefactoOdontoGate.exception.ScheduleExceptions;
import com.OdontoGate.ArtefactoOdontoGate.model.Doctor;
import com.OdontoGate.ArtefactoOdontoGate.model.Schedule;
import com.OdontoGate.ArtefactoOdontoGate.repository.DoctorRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;

    public ScheduleResponse create(ScheduleRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Doctor no encontrado con ID: " + request.getDoctorId()));

        Schedule schedule = new Schedule();
        schedule.setWeekday(request.getWeekday());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setIsAvailable(request.getIsAvailable());
        schedule.setDoctor(doctor);

        validarHorario(request.getStartTime(), request.getEndTime());

        Schedule saved = scheduleRepository.save(schedule);
        return toResponse(saved);
    }

    public List<ScheduleResponse> getAll() {
        return scheduleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ScheduleResponse> getByDoctor(Integer doctorId) {
        return scheduleRepository.findByDoctorId(doctorId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void delete(Integer id) {
        scheduleRepository.deleteById(id);
    }

    public ScheduleResponse update(Integer id, ScheduleRequest request) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        if (request.getWeekday() != null) schedule.setWeekday(request.getWeekday());
        if (request.getStartTime() != null) schedule.setStartTime(request.getStartTime());
        if (request.getEndTime() != null) schedule.setEndTime(request.getEndTime());
        if (request.getIsAvailable() != null) schedule.setIsAvailable(request.getIsAvailable());

        LocalTime startFinal = request.getStartTime() != null ? request.getStartTime() : schedule.getStartTime();
        LocalTime endFinal = request.getEndTime() != null ? request.getEndTime() : schedule.getEndTime();

        validarHorario(startFinal, endFinal);

        Schedule saved = scheduleRepository.save(schedule);
        return toResponse(saved);
    }

    public ScheduleResponse setAvailable(Integer id, Boolean available) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        schedule.setIsAvailable(available);

        Schedule saved = scheduleRepository.save(schedule);
        return toResponse(saved);
    }

    private void validarHorario(LocalTime startTime, LocalTime endTime) {
        if (!startTime.isBefore(endTime)) {
            throw new ScheduleExceptions.InvalidScheduleTimeException();
        }
    }

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