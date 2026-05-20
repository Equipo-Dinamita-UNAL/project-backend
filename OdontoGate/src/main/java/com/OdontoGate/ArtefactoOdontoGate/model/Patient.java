package com.OdontoGate.ArtefactoOdontoGate.model;


import java.sql.Date;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "\"patient\"")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "birth_date")
    private Date birth_date;

    @Column(name = "blood_type")
    private String blood_type;

    @Column(name = "allergies")
    private String allergies;

    @Column(name = "address")
    private String address;
    
}
