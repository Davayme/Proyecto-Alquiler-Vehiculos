package com.car.rental.demo.Models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;

    private String brand;
    private String model;
    private String licensePlate; // Matricula
    private String type;
    private String status; // Available, In Maintenance, Rented
    private double dailyRate;

    @Temporal(TemporalType.DATE)
    private Date acquisitionDate;

    private double mileage;
    private String location;

    // Getters and setters
}