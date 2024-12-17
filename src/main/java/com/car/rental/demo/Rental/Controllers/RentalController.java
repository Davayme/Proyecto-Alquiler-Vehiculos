package com.car.rental.demo.Rental.Controllers;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.car.rental.demo.Models.Payment;
import com.car.rental.demo.Models.Rental;
import com.car.rental.demo.Rental.Dtos.PaymentDTO;
import com.car.rental.demo.Rental.Dtos.RentalDTO;
import com.car.rental.demo.Rental.Services.PaymentService;
import com.car.rental.demo.Stripe.StripeService;
import com.stripe.model.PaymentIntent;
// import com.car.rental.demo.Vehicles.Dtos.VehicleGet;
import com.stripe.model.checkout.Session;

import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/rentals")
@Validated
public class RentalController {
    @Autowired
    private PaymentService rentalService;
    @Autowired
    private StripeService stripeService;
    @PostMapping
    public ResponseEntity<?> createRental(@RequestBody RentalDTO rentalDTO) {
        try {
            Rental rental = rentalService.createRental(rentalDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(rental);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la renta: " + ex.getMessage());
        }
    }

    @PostMapping("/payment")
    public ResponseEntity<?> createPayment(@RequestBody PaymentDTO paymentDTO) {
        try {
            Session session = stripeService.getSessionDetails(paymentDTO.getSessionId());
            String stripeId = session.getPaymentIntent();
            PaymentIntent paymentIntent = PaymentIntent.retrieve(stripeId);
            String paymentMethod = paymentIntent.getPaymentMethodTypes().get(0);

            paymentDTO.setStripeId(stripeId);
            paymentDTO.setPaymentMethod(paymentMethod);
            paymentDTO.setAmount((Double)(session.getAmountTotal() / 100.0));
            paymentDTO.setRentalId(Long.parseLong(session.getMetadata().get("rentalId")));

            Payment payment = rentalService.createPayment(paymentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(payment);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el pago: " + ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Rental>> getAllRental() {
        List<Rental> rentals = rentalService.getRentals();
        return ResponseEntity.ok(rentals);
    }
    
}
