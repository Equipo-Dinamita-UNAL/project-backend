package com.OdontoGate.ArtefactoOdontoGate.repository;

import com.OdontoGate.ArtefactoOdontoGate.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    // Buscar todos los horarios de un doctor específico
        List<Schedule> findByDoctorId(Integer doctorId);

    // Buscar horarios de un doctor en un día específico
        List<Schedule> findByDoctorIdAndWeekday(Integer doctorId, String weekday);

    // Buscar solo los horarios disponibles de un doctor
        List<Schedule> findByDoctorIdAndIsAvailable(Integer doctorId, Boolean isAvailable);

}