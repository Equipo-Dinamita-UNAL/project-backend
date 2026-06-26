package com.OdontoGate.ArtefactoOdontoGate.model;

import jakarta.persistence.*;
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