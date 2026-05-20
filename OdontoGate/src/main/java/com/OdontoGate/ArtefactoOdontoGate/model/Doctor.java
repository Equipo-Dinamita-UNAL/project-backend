package com.OdontoGate.ArtefactoOdontoGate.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "\"dcotor\"")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "speciality")
    private String birth_date;

    @Column(name = "medical_license")
    private String medical_license;

    @Column(name = "photo_url")
    private String photo_url;
}
