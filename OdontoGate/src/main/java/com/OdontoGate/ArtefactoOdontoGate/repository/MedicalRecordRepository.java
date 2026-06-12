package com.OdontoGate.ArtefactoOdontoGate.repository;

import com.OdontoGate.ArtefactoOdontoGate.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface MedicalRecordRepository
        extends JpaRepository<MedicalRecord, Integer> {

    List<MedicalRecord> findByPatientId(Integer patientId);

}