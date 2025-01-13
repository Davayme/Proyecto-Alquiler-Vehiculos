package com.car.rental.demo.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "rentalId")
    private Rental rental;

    @ManyToOne
    @JoinColumn(name = "returnId")
    private Return returnRecord; 
    
    private String stripePaymentId; // Guarda el ID del PaymentIntent de Stripe
    private String paymentMethod;   // Guarda el método: card, cash, transfer, etc.
    private double amount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;
    @Enumerated(EnumType.STRING)
    private TypePayment typePayment;

    public enum TypePayment {
        RENTAL, RETURN
    }
}