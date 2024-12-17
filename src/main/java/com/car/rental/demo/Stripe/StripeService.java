package com.car.rental.demo.Stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
// import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
// import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public Session createCheckoutSession(Long rentalId, double amount, String successUrl, String cancelUrl) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        // Configuración de la sesión
        SessionCreateParams params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT) // Modo de pago
            .setSuccessUrl(successUrl)                // URL de éxito
            .setCancelUrl(cancelUrl)                  // URL de cancelación
            .putAllMetadata(Map.of(
                "rentalId", rentalId.toString() // Agrega datos personalizados como rentalId
            ))
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)                  // Cantidad (siempre 1 para servicios)
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("usd")       // Moneda
                            .setUnitAmount((long) (amount * 100)) // Monto en centavos
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName("Pago por la renta")   // Nombre del producto
                                    .setDescription("Pago por la renta con ID: " + rentalId)
                                    .build()
                            )
                            
                            .build()
                    )
                    
                    .build()
            )
            .build();

        // Crea la sesión
        return Session.create(params);
    }

    public Session getSessionDetails(String sessionId) throws StripeException {
        Stripe.apiKey = stripeApiKey;
        return Session.retrieve(sessionId);
    }
}
