package com.car.rental.demo.Stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public PaymentIntent createPaymentIntent(double amount, String currency) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        PaymentIntentCreateParams params =
            PaymentIntentCreateParams.builder()
                .setAmount((long) (amount * 100)) // Stripe usa centavos, multiplica por 100
                .setCurrency(currency)
                .setAutomaticPaymentMethods(
                    PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                        .setEnabled(true)
                        .build()
                )
                .build();

        return PaymentIntent.create(params);
    }
}
