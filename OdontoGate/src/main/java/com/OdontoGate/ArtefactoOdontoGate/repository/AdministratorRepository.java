package com.OdontoGate.ArtefactoOdontoGate.repository;


import org.springframework.stereotype.Repository;
import com.OdontoGate.ArtefactoOdontoGate.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Define el contrato publico de AdministratorRepository.
 */
@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

}