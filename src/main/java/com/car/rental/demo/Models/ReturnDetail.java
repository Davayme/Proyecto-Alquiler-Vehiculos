package com.car.rental.demo.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "return_details")
public class ReturnDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailId;

    @ManyToOne
    @JoinColumn(name = "returnId", nullable = false)
    @JsonBackReference
    private Return returnRecord;

    private String partName; // Nombre de la parte del vehículo (e.g., "Door", "Tire")

    @Enumerated(EnumType.STRING)
    private PartStatus status; // "PERFECT" o "DAMAGED"

    private double damageCost;

    public enum PartStatus {
        PERFECT,
        DAMAGED
    }
}