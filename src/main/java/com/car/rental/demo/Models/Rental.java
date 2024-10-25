package com.car.rental.demo.Models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalId;

    @ManyToOne
    @JoinColumn(name = "clientId")
    private User client; // Relación con la tabla Users (cliente)

    @ManyToOne
    @JoinColumn(name = "vehicleId")
    private Vehicle vehicle; // Relación con la tabla Vehiculos

    @ManyToOne
    @JoinColumn(name = "employeeId")
    private User employee; // Relación con la tabla Users (empleado que gestionó el alquiler)

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    private double totalAmount;
    private String status; // Reserved, In Progress, Completed

    // Getters and setters
}