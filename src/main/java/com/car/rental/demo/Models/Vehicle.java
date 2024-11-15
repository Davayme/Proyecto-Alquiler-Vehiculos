package com.car.rental.demo.Models;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;

    private String brand;
    private String model;
    private String licensePlate; // Matricula

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleStatus status; // Available, In Maintenance, Rented

    @Builder.Default
    private boolean active = true;

    @Temporal(TemporalType.DATE)
    private Date acquisitionDate;

    private double mileage;
    private String location;

    @ManyToOne
    @JoinColumn(name = "typeId")
    @JsonBackReference
    private TypeVehicle type;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<VehicleImage> images;

    public enum VehicleStatus {
        AVAILABLE, // Disponible
        IN_MAINTENANCE, // En mantenimiento
        RENTED // Alquilado
    }
}