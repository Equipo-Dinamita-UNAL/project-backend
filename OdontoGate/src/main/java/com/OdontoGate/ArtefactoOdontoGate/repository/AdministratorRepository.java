package com.OdontoGate.ArtefactoOdontoGate.repository;


import java.util.List;
import org.springframework.stereotype.Repository;
import com.OdontoGate.ArtefactoOdontoGate.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

    List<Administrator> findByActiveTrue();

    List<Administrator> findByActiveFalse();
}
