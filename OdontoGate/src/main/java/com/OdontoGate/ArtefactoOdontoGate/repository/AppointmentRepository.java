package com.OdontoGate.ArtefactoOdontoGate.repository;

import com.OdontoGate.ArtefactoOdontoGate.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.time.LocalTime;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    // Buscar todas las citas de un paciente
        List<Appointment> findByPatientId(Integer patientId);

    // Buscar todas las citas de un doctor
        List<Appointment> findByDoctorId(Integer doctorId);

    // Buscar citas por fecha
        List<Appointment> findByDate(LocalDate date);

    // Buscar citas de un doctor en una fecha específica
        List<Appointment> findByDoctorIdAndDate(Integer doctorId, LocalDate date);

    // Buscar citas por estado (ej: "pendiente", "cancelada")
        List<Appointment> findByStatus(String status);

        List<Appointment> findByDoctorIdAndDateAndTime(Integer doctorId, LocalDate date, LocalTime time);
}