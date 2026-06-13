package com.OdontoGate.ArtefactoOdontoGate.dto.request;

import lombok.Data;

@Data
public class ReceiptRequest {
    private Integer paymentId;
    private String type;

}
