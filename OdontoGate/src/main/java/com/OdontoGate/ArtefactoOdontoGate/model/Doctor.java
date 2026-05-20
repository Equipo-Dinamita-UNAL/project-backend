package com.OdontoGate.ArtefactoOdontoGate.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "\"doctor\"")
@PrimaryKeyJoinColumn(name = "id")
public class Doctor extends User {
    
    @Column(name = "speciality")
    private String speciality;

    @Column(name = "medicalLicense")
    private String medicalLicense;

    @Column(name = "photoUrl")
    private String photoUrl;
}
