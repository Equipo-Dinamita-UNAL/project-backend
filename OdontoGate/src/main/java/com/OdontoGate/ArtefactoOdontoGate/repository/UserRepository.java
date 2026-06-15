package com.OdontoGate.ArtefactoOdontoGate.repository;

import com.OdontoGate.ArtefactoOdontoGate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Define el contrato publico de UserRepository.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Ejecuta la operacion publica existsByEmail.
     */
    boolean existsByEmail(String email);
    /**
     * Ejecuta la operacion publica findByEmailAndPassword.
     */
    User findByEmailAndPassword(String email, String password);

    /**
     * Ejecuta la operacion publica findByEmail.
     */
    User findByEmail(String email);
}

