package com.OdontoGate.ArtefactoOdontoGate.repository;

import org.springframework.stereotype.Repository;
import com.OdontoGate.ArtefactoOdontoGate.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
}
