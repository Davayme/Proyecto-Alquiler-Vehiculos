package com.car.rental.demo.Rental.Controllers;


import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.car.rental.demo.Models.Client;
import com.car.rental.demo.Models.Payment;
import com.car.rental.demo.Models.Rental;
import com.car.rental.demo.Models.Vehicle;
import com.car.rental.demo.Models.Payment.TypePayment;
import com.car.rental.demo.Models.Rental.RentalStatus;
import com.car.rental.demo.Models.Vehicle.VehicleStatus;
import com.car.rental.demo.Rental.Dtos.PaymentDTO;
import com.car.rental.demo.Rental.Dtos.RentalDTO;
import com.car.rental.demo.Rental.Dtos.ResponseRentalDTO;
import com.car.rental.demo.Rental.Services.PaymentService;
import com.car.rental.demo.Stripe.StripeService;
import com.car.rental.demo.Vehicles.VehicleRepository;
import com.car.rental.demo.Vehicles.Services.VehicleService;
import com.stripe.model.PaymentIntent;
// import com.car.rental.demo.Vehicles.Dtos.VehicleGet;
import com.stripe.model.checkout.Session;

import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/rentals")
@Validated
public class RentalController {
    @Autowired
    private PaymentService rentalService;
    @Autowired
    private StripeService stripeService;
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @PostMapping
    public ResponseEntity<?> createRental(@RequestBody RentalDTO rentalDTO) {
        try {
            Rental rental = rentalService.createRental(rentalDTO);
            if (rental != null) {
                Vehicle vehicle = rental.getVehicle();
                vehicle.setStatus(VehicleStatus.RENTED);
                vehicleRepository.save(vehicle);
            } 
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
            if (session.getMetadata().get("typePayment") == "RETURN") {
                paymentDTO.setTypePayment(TypePayment.RETURN);
            }else {
                paymentDTO.setTypePayment(TypePayment.RENTAL);
            }
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

    @GetMapping("/{email}")
    public ResponseEntity<List<ResponseRentalDTO>> getAllRentalByUser(@PathVariable("email")  String email) {
        List<ResponseRentalDTO> rentals = rentalService.getRentals(email);
        return ResponseEntity.ok(rentals);
    }

    @GetMapping
    public ResponseEntity<List<ResponseRentalDTO>> getAllRental() {
        List<ResponseRentalDTO> rentals = rentalService.getRentalsActive();
        return ResponseEntity.ok(rentals);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putMethodName(@PathVariable("id")  Long id, @RequestBody  Map<String, String> request) {
       try {
            String status = request.get("status");
            Rental rental = rentalService.updateStatusRental(id, status);
            return ResponseEntity.ok(rental);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + ex.getMessage());
        }
    }
}
