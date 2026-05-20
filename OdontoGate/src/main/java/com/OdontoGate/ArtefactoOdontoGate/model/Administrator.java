package com.OdontoGate.ArtefactoOdontoGate.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "\"administrator\"")
@PrimaryKeyJoinColumn(name = "id")
public class Administrator extends User {
    
    @Column(name = "position")
    private String position;
}
