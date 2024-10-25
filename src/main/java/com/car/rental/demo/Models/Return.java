package com.car.rental.demo.Models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Return {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long returnId;

    @ManyToOne
    @JoinColumn(name = "rentalId")
    private Rental rental;

    @Temporal(TemporalType.TIMESTAMP)
    private Date returnDate;

    private String vehicleCondition;
    private double extraCharges;

    // Getters and setters
}