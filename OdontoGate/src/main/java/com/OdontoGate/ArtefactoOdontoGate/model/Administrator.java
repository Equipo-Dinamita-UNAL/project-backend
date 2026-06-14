package com.OdontoGate.ArtefactoOdontoGate.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Define el contrato publico de Administrator.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "\"administrator\"")
@PrimaryKeyJoinColumn(name = "id")
public class Administrator extends User {

    @Column(name = "position")
    private String position;
}
