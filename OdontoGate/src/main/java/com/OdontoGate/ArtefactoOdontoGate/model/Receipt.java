package com.OdontoGate.ArtefactoOdontoGate.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "\"receipt\"")
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(name = "type", nullable = false)
    private String type;


    @Column(name = "pdf_url", nullable = true)
    private String pdf_url;

    @Column(name = "issue_date", nullable = true)
    private LocalDateTime issue_date;

    @Column(name = "created_at", nullable = true)
    private LocalDateTime createdAt;
}
