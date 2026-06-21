package com.OdontoGate.ArtefactoOdontoGate.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PaymentApprovedEvent {
    private final Integer paymentId;
}
