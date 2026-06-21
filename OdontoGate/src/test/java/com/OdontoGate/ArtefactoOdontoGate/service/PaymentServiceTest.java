package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.request.PaymentRequest;
import com.OdontoGate.ArtefactoOdontoGate.model.Appointment;
import com.OdontoGate.ArtefactoOdontoGate.model.Payment;
import com.OdontoGate.ArtefactoOdontoGate.repository.AppointmentRepository;
import com.OdontoGate.ArtefactoOdontoGate.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void cuandoCitaYaTienePago_debeLanzarExcepcion() {


        PaymentRequest request = new PaymentRequest();
        request.setAppointmentId(1);
        request.setMethod("EFECTIVO");

        Appointment appointment = new Appointment();
        appointment.setId(1);

        Payment pagoExistente = new Payment();
        pagoExistente.setId(1);
        pagoExistente.setStatus("PENDIENTE");


        when(appointmentRepository.findById(1))
                .thenReturn(Optional.of(appointment));
        when(paymentRepository.findByAppointmentId(1))
                .thenReturn(Optional.of(pagoExistente));


        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> paymentService.createPayment(request));

        assertEquals("Esta cita ya tiene un pago", ex.getMessage());
    }

    @Test
    void cuandoCitaNoExiste_debeLanzarExcepcion() {


        PaymentRequest request = new PaymentRequest();
        request.setAppointmentId(99);
        request.setMethod("EFECTIVO");


        when(appointmentRepository.findById(99))
                .thenReturn(Optional.empty());


        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> paymentService.createPayment(request));

        assertEquals("Cita no encontrada con id: 99", ex.getMessage());
    }



}
