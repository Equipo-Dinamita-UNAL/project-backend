package com.OdontoGate.ArtefactoOdontoGate.repository;

import com.OdontoGate.ArtefactoOdontoGate.model.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Integer> {
    // Buscador clave que usaremos en el servicio
    Optional<Treatment> findByName(String name);
}