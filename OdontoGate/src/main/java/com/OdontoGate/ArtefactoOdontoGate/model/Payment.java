package com.OdontoGate.ArtefactoOdontoGate.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "\"payment\"")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

     //espacio para relacionar con citas

     //espacio para relacionar con pacientes

    @OneToMany(mappedBy = "payment")
    private List<Receipt> receipts;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "method", nullable = true)
    private String method;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "gateway_reference", nullable = true)
    private String gateway_reference;

    @Column(name = "created_at", nullable = true)
    private LocalDateTime createdAt;
}
