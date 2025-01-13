package com.car.rental.demo.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "returns")
public class Return {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long returnId;

    @OneToOne
    @JoinColumn(name = "rentalId", nullable = false)
    private Rental rental;

    private Date returnDate;

    private double totalReturnAmount;

    private double lateFee;

    @OneToMany(mappedBy = "returnRecord", cascade = CascadeType.ALL)
    @Builder.Default
    @JsonManagedReference
    private List<ReturnDetail> details = new ArrayList<>();

    // Método para calcular el monto total de los daños
    public double calculateTotalDamageCost() {
        return details.stream()
                .filter(detail -> ReturnDetail.PartStatus.DAMAGED.equals(detail.getStatus()))
                .mapToDouble(ReturnDetail::getDamageCost)
                .sum();
    }

    // Método para actualizar el monto total de los daños
    @PrePersist
    @PreUpdate
    public void updateTotalReturnAmount() {
        this.totalReturnAmount = calculateTotalDamageCost() + this.lateFee;
    }
}