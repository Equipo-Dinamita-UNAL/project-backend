package com.OdontoGate.ArtefactoOdontoGate.repository;

import org.springframework.stereotype.Repository;
import com.OdontoGate.ArtefactoOdontoGate.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
}
