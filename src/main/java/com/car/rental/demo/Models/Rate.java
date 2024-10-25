package com.car.rental.demo.Models;

import jakarta.persistence.*;

@Entity
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rateId;

    @ManyToOne
    @JoinColumn(name = "vehicleId")
    private Vehicle vehicle;

    private String season; // High, Low
    private String rentalDuration; // Daily, Weekly, Monthly
    private double cost;

    // Getters and setters
}