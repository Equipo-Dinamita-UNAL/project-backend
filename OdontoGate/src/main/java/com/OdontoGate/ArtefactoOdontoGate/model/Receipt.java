package com.OdontoGate.ArtefactoOdontoGate.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    @Column(name = "receipt_number", nullable = false)
    private String receiptNumber;

    @Column(name = "type", nullable = false)
    private String type;


    @Column(name = "pdf_url", nullable = true)
    private String pdfUrl;

    @Column(name = "issue_date", nullable = true)
    private LocalDateTime issueDate;

    @Column(name = "created_at", nullable = true)
    private LocalDateTime createdAt;
}
