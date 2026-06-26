package com.OdontoGate.ArtefactoOdontoGate.repository;

import com.OdontoGate.ArtefactoOdontoGate.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer>{

        Optional<Payment> findByAppointmentId(Integer appointmentId);
        List<Payment> findByAppointmentPatientId(Integer patientId);
        List<Payment> findByStatus(String status);
        List<Payment> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
