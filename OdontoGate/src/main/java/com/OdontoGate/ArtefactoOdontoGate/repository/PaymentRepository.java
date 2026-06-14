package com.OdontoGate.ArtefactoOdontoGate.repository;

import com.OdontoGate.ArtefactoOdontoGate.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Define el contrato publico de PaymentRepository.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer>{

    /**
     * Ejecuta la operacion publica findByAppointmentId.
     */
    Optional<Payment> findByAppointmentId(Integer appointmentId);
    /**
     * Ejecuta la operacion publica findByAppointmentPatientId.
     */
    List<Payment> findByAppointmentPatientId(Integer patientId);
    /**
     * Ejecuta la operacion publica findByStatus.
     */
    List<Payment> findByStatus(String status);
    /**
     * Ejecuta la operacion publica findByCreatedAtBetween.
     */
    List<Payment> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
