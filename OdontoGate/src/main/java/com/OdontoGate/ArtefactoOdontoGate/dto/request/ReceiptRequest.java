package com.OdontoGate.ArtefactoOdontoGate.dto.request;

import lombok.Data;

/**
 * Define el contrato publico de ReceiptRequest.
 */
@Data
public class ReceiptRequest {
    private Integer paymentId;
    private String type;

}
