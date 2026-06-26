package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.exception.MercadoPagoIntegrationException;
import com.OdontoGate.ArtefactoOdontoGate.gateway.PaymentGateway;
import com.OdontoGate.ArtefactoOdontoGate.gateway.PaymentInfo;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.resources.preference.Preference;


import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;

@Service
public class MercadoPagoService implements PaymentGateway {

    @Override
    public String generateCheckoutUrl(String referenceId, BigDecimal amount, String title) {
        try {
            MPRequestOptions requestOptions = MPRequestOptions.builder()
                    .accessToken(MercadoPagoConfig.getAccessToken())
                    .build();

            PreferenceClient client = new PreferenceClient();

            // 1. Creamos el item a cobrar (La cita odontológica)
            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .title(title)
                    .quantity(1)
                    .unitPrice(amount)
                    .currencyId("COP") // Cambia a MXN, ARS, CLP según tu país
                    .build();

            // 2. Creamos la preferencia
            PreferenceRequest request = PreferenceRequest.builder()
                    .items(Collections.singletonList(item))
                    .externalReference(referenceId) // ¡CLAVE! MP nos devolverá este ID en el Webhook
                    .notificationUrl("https://cloak-unblended-reverence.ngrok-free.dev/api/payment/webhook")
                    .build();

            // 3. Enviamos la petición a Mercado Pago
            Preference preference = client.create(request, requestOptions);

            // 5. CRÍTICO: Usamos Sandbox (entorno de pruebas) para evitar bloqueos
            return preference.getInitPoint();

        } catch (com.mercadopago.exceptions.MPApiException apiException) {
            System.err.println("❌ Detalle exacto: " + apiException.getApiResponse().getContent());
            throw new MercadoPagoIntegrationException("Rechazado por Mercado Pago", apiException);

        } catch (Exception e) {
            e.printStackTrace();
            throw new MercadoPagoIntegrationException("Error al comunicarse con la API de Mercado Pago al crear la preferencia", e);
        }
    }

    @Override
    public PaymentInfo getPaymentInfo(String mercadoPagoPaymentId) {
        try {
            MPRequestOptions requestOptions = MPRequestOptions.builder()
                    .accessToken(MercadoPagoConfig.getAccessToken())
                    .build();

            PaymentClient client = new PaymentClient();
            com.mercadopago.resources.payment.Payment payment =
                    client.get(Long.parseLong(mercadoPagoPaymentId), requestOptions);

            return new PaymentInfo(payment.getStatus(), payment.getExternalReference());

        } catch (Exception e) {
            throw new MercadoPagoIntegrationException("Error al consultar el pago", e);
        }
    }


}
