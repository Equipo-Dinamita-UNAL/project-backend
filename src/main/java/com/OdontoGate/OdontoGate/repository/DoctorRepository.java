package com.OdontoGate.OdontoGate.repository;

import org.springframework.stereotype.Repository;
import com.OdontoGate.OdontoGate.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
}