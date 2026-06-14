package com.OdontoGate.ArtefactoOdontoGate.repository;

import com.OdontoGate.ArtefactoOdontoGate.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.time.LocalTime;

/**
 * Define el contrato publico de AppointmentRepository.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    // Buscar todas las citas de un paciente
    /**
     * Ejecuta la operacion publica findByPatientId.
     */
    List<Appointment> findByPatientId(Integer patientId);

    // Buscar todas las citas de un doctor
    /**
     * Ejecuta la operacion publica findByDoctorId.
     */
    List<Appointment> findByDoctorId(Integer doctorId);

    // Buscar citas por fecha
    /**
     * Ejecuta la operacion publica findByDate.
     */
    List<Appointment> findByDate(LocalDate date);

    // Buscar citas de un doctor en una fecha específica
    /**
     * Ejecuta la operacion publica findByDoctorIdAndDate.
     */
    List<Appointment> findByDoctorIdAndDate(Integer doctorId, LocalDate date);

    // Buscar citas por estado (ej: "pendiente", "cancelada")
    /**
     * Ejecuta la operacion publica findByStatus.
     */
    List<Appointment> findByStatus(String status);

    /**
     * Ejecuta la operacion publica findByDoctorIdAndDateAndTime.
     */
    List<Appointment> findByDoctorIdAndDateAndTime(Integer doctorId, LocalDate date, LocalTime time);
}