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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;


@Service
@RequiredArgsConstructor
public class MercadoPagoService implements PaymentGateway {

    private final PreferenceClient preferenceClient;

    @Value("${mercadopago.currency}")
    private String currencyId;

    @Value("${mercadopago.webhook.url}")
    private String notificationUrl;

    @Override
    public String generateCheckoutUrl(String referenceId, BigDecimal amount, String title) {
        try {
            MPRequestOptions requestOptions = MPRequestOptions.builder()
                    .accessToken(MercadoPagoConfig.getAccessToken())
                    .build();

            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .title(title)
                    .quantity(1)
                    .unitPrice(amount)
                    .currencyId(currencyId)
                    .build();

            PreferenceRequest request = PreferenceRequest.builder()
                    .items(Collections.singletonList(item))
                    .externalReference(referenceId)
                    .notificationUrl(notificationUrl)
                    .build();

            Preference preference = preferenceClient.create(request, requestOptions);

            return preference.getInitPoint();

        } catch (com.mercadopago.exceptions.MPApiException apiException) {
            String reason = "Sin detalles";

            if (apiException.getApiResponse() != null) {
                reason = apiException.getApiResponse().getContent();
            }

            throw new MercadoPagoIntegrationException("Rechazado por Mercado Pago. Motivo: " + reason, apiException);

        } catch (Exception e) {
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
