package com.car.rental.demo.Returns.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.car.rental.demo.Models.Return;
import com.car.rental.demo.Models.Payment;
import com.car.rental.demo.Models.Payment.TypePayment;
import com.car.rental.demo.Rental.Dtos.PaymentDTO;
import com.car.rental.demo.Rental.Services.PaymentService;
import com.car.rental.demo.Returns.Dtos.CreateReturnDTO;
import com.car.rental.demo.Returns.Dtos.CreateReturnWithoutDamageDTO;
import com.car.rental.demo.Returns.Dtos.PaymentDTOReturn;
import com.car.rental.demo.Returns.Dtos.RentalDtoReturns;
import com.car.rental.demo.Returns.Services.ReturnService;
import com.car.rental.demo.Stripe.StripeService;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;

@RestController
@RequestMapping("/returns")
public class ReturnController {

    @Autowired
    private ReturnService returnService;

    @Autowired
    private StripeService stripeService;

   

    @GetMapping
    public List<RentalDtoReturns> getAllRentals() {
        return returnService.getAllRentals();
    }

    @PostMapping("/without-damage")
    public Return createReturnWithoutDamage(@RequestBody CreateReturnWithoutDamageDTO createReturnWithoutDamageDTO) {
        return returnService.createReturnWithoutDamage(createReturnWithoutDamageDTO);
    }

    @PostMapping("/with-damage")
    public Return createReturnWithDamage(@RequestBody CreateReturnDTO createReturnDTO) {
        return returnService.createReturnWithDamage(createReturnDTO);
    }

    @PostMapping("/payment")
    public ResponseEntity<?> createReturnPayment(@RequestBody PaymentDTOReturn paymentDTOReturn) {
        try {
            Session session = stripeService.getSessionDetails(paymentDTOReturn.getSessionId());
            String stripeId = session.getPaymentIntent();
            PaymentIntent paymentIntent = PaymentIntent.retrieve(stripeId);
            String paymentMethod = paymentIntent.getPaymentMethodTypes().get(0);

            // Configurar detalles del pago
            paymentDTOReturn.setTypePayment(TypePayment.RETURN);
            paymentDTOReturn.setStripeId(stripeId);
            paymentDTOReturn.setPaymentMethod(paymentMethod);
            paymentDTOReturn.setAmount(session.getAmountTotal() / 100.0);
            paymentDTOReturn.setReturnId(paymentDTOReturn.getReturnId());

            Payment payment = returnService.createPayment(paymentDTOReturn);
            return ResponseEntity.status(HttpStatus.CREATED).body(payment);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el pago: " + ex.getMessage());
        }
    }

}