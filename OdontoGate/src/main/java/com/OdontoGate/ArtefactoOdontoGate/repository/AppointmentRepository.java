package com.OdontoGate.ArtefactoOdontoGate.repository;

import com.OdontoGate.ArtefactoOdontoGate.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.time.LocalTime;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    
        List<Appointment> findByPatientId(Integer patientId);

    
        List<Appointment> findByDoctorId(Integer doctorId);

   
        List<Appointment> findByDate(LocalDate date);

    
        List<Appointment> findByDoctorIdAndDate(Integer doctorId, LocalDate date);

   
        List<Appointment> findByStatus(String status);

        List<Appointment> findByDoctorIdAndDateAndTime(Integer doctorId, LocalDate date, LocalTime time);

        @Query("""
                select a
                from Appointment a
                join fetch a.patient
                join fetch a.doctor
                where a.doctor.id = :doctorId
                and (a.status is null or lower(a.status) <> 'cancelada')
                order by a.patient.lastname, a.patient.name, a.date, a.time
                """)
        List<Appointment> findScheduledAppointmentsByDoctorId(@Param("doctorId") Integer doctorId);
}
