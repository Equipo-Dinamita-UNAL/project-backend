package com.OdontoGate.OdontoGate.repository;

import org.springframework.stereotype.Repository;
import com.OdontoGate.OdontoGate.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
}