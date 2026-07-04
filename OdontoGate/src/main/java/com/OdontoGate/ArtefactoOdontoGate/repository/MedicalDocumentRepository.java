package com.OdontoGate.ArtefactoOdontoGate.repository;

import com.OdontoGate.ArtefactoOdontoGate.model.MedicalDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalDocumentRepository extends JpaRepository<MedicalDocument, Integer> {

    List<MedicalDocument> findByPatientId(Integer patientId);

    List<MedicalDocument> findByMedicalRecordId(Integer medicalRecordId);

    List<MedicalDocument> findByPatientIdAndMedicalRecordId(Integer patientId, Integer medicalRecordId);
}