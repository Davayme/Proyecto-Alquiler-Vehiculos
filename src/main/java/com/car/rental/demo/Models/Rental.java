package com.car.rental.demo.Models;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalId;

    @ManyToOne
    @JoinColumn(name = "clientId")
    private Client client; // Relación con la tabla Users (cliente)

    @ManyToOne
    @JoinColumn(name = "vehicleId")
    private Vehicle vehicle; // Relación con la tabla Vehiculos

    // @ManyToOne
    // @JoinColumn(name = "employeeId")
    // private User employee; // Relación con la tabla Users (empleado que gestionó el alquiler)
    @Temporal(TemporalType.TIMESTAMP)
    private Date rentalDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date returnDate;
    private String rentalDuration;

    private int quantityOfDuration;

    private double totalAmount;
    @Enumerated(EnumType.STRING)
    private RentalStatus status; // Reserved, In Progress, Completed
    
    @Builder.Default
    private boolean active = true;


    public enum RentalStatus {
        RESERVED, 
        IN_PROGRESS, 
        COMPLETED,
        CANCELLED
    }
   
}