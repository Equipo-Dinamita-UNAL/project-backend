package com.OdontoGate.ArtefactoOdontoGate.model;


import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * Define el contrato publico de Patient.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "\"patient\"")
@PrimaryKeyJoinColumn(name = "id")
public class Patient extends User {

    @Column(name = "birthDate")
    private LocalDate birthDate;

    @Column(name = "bloodType")
    private String bloodType;

    @Column(name = "allergies")
    private String allergies;

    @Column(name = "address")
    private String address;

}
