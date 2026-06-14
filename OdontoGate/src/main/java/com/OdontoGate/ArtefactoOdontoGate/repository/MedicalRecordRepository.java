package com.OdontoGate.ArtefactoOdontoGate.repository;

import com.OdontoGate.ArtefactoOdontoGate.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

/**
 * Define el contrato publico de MedicalRecordRepository.
 */
@Repository
public interface MedicalRecordRepository
        extends JpaRepository<MedicalRecord, Integer> {

    /**
     * Ejecuta la operacion publica findByPatientId.
     */
    List<MedicalRecord> findByPatientId(Integer patientId);

}