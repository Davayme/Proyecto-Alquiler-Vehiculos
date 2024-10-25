package com.car.rental.demo.Models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "rentalId")
    private Rental rental;

    private String paymentMethod; // Cash, Card, Transfer
    private double amount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;

    private String paymentStatus; // Complete, Pending, Canceled

    // Getters and setters
}