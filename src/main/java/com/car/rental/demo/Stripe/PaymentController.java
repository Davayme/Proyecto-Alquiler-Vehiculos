package com.car.rental.demo.Stripe;


import com.stripe.exception.StripeException;

import com.stripe.model.checkout.Session;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe")
public class PaymentController {

    @Autowired
    private StripeService stripeService;

    @PostMapping("/checkout-session")
    public ResponseEntity<?> createCheckoutSession(@RequestBody SessionDTO sessionDTO) {
        try {
            // URLs de redirección
            String successUrl = sessionDTO.getSuccessUrl(); // Cambia según tu frontend
            String cancelUrl = sessionDTO.getCancelUrl(); // Cambia según tu frontend

            // Crea la sesión
            Session session = stripeService.createCheckoutSession(sessionDTO.getRentalId(), sessionDTO.getAmount(), successUrl, cancelUrl);

            // Devuelve la URL de la sesión
            return ResponseEntity.ok(Map.of(
                "url", session.getUrl(),
                "sessionId", session.getId()
            ));
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
