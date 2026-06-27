package com.OdontoGate.ArtefactoOdontoGate.repository;

import com.OdontoGate.ArtefactoOdontoGate.model.Payment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer>{

        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("SELECT p FROM Payment p WHERE p.id = :id")
        Optional<Payment> findByIdForUpdate(@Param("id") Integer id);

        Optional<Payment> findByAppointmentId(Integer appointmentId);
        List<Payment> findByAppointmentPatientId(Integer patientId);
        List<Payment> findByStatus(String status);
        List<Payment> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
