package com.OdontoGate.OdontoGate.repository;


import org.springframework.stereotype.Repository;
import com.OdontoGate.OdontoGate.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

}