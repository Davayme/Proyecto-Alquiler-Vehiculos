package com.car.rental.demo.Stripe;


import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe")
public class PaymentController {

    @Autowired
    private StripeService stripeService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestParam double amount, @RequestParam String currency) {
        try {
            PaymentIntent paymentIntent = stripeService.createPaymentIntent(amount, currency);
            return ResponseEntity.ok(paymentIntent);
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
