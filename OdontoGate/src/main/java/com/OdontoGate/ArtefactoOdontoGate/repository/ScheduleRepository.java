package com.OdontoGate.ArtefactoOdontoGate.repository;

import com.OdontoGate.ArtefactoOdontoGate.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Define el contrato publico de ScheduleRepository.
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    // Buscar todos los horarios de un doctor específico
    /**
     * Ejecuta la operacion publica findByDoctorId.
     */
    List<Schedule> findByDoctorId(Integer doctorId);

    // Buscar horarios de un doctor en un día específico
    /**
     * Ejecuta la operacion publica findByDoctorIdAndWeekday.
     */
    List<Schedule> findByDoctorIdAndWeekday(Integer doctorId, String weekday);

    // Buscar solo los horarios disponibles de un doctor
    /**
     * Ejecuta la operacion publica findByDoctorIdAndIsAvailable.
     */
    List<Schedule> findByDoctorIdAndIsAvailable(Integer doctorId, Boolean isAvailable);

}