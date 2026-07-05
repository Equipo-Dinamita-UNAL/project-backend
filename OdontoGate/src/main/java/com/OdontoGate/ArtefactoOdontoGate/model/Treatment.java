package com.OdontoGate.ArtefactoOdontoGate.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "treatment")
public class Treatment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Nombre único del tratamiento (ej: "Consulta General", "Ortodoncia")
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    // El precio asignado en Pesos Colombianos
    @Column(name = "price", nullable = false)
    private Double price;
}
