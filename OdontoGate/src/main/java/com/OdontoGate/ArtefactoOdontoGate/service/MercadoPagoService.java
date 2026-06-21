package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.exception.MercadoPagoIntegrationException;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;

@Service
public class MercadoPagoService implements PaymentGateway {

    @Override
    public String generateCheckoutUrl(String referenceId, BigDecimal amount, String title) {
        try {
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
                    .build();

            // 3. Enviamos la petición a Mercado Pago
            Preference preference = client.create(request);

            // 4. Retornamos la URL donde el paciente debe pagar
            return preference.getInitPoint();

        } catch (Exception e) {
            throw new MercadoPagoIntegrationException("Error al comunicarse con la API de Mercado Pago al crear la preferencia", e);
        }
    }


}
