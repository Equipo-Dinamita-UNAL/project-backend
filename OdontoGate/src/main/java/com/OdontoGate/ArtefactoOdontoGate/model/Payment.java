package com.OdontoGate.ArtefactoOdontoGate.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

    @OneToOne
    @JoinColumn(name = "appointment_id", referencedColumnName = "id", nullable = false)
    private Appointment appointment;

    @OneToMany(mappedBy = "payment")
    private List<Receipt> receipts;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "method", nullable = true)
    private String method;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "gateway_reference", nullable = true)
    private String gatewayReference;

    @Column(name = "created_at", nullable = true)
    private LocalDateTime createdAt;
}
