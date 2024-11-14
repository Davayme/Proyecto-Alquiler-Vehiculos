
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
@Table(name = "rates", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"typeId", "season", "rentalDuration"})
})
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rateId;

    @ManyToOne
    @JoinColumn(name = "typeId", nullable = false)
    private TypeVehicle type; // Relación con el tipo de vehículo

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Season season; // Temporada: HIGH, LOW

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalDuration rentalDuration; // Duración: DAILY, WEEKLY, MONTHLY

    @Column(nullable = false)
    private double cost; // Precio de la tarifa

    @Builder.Default
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean active = true;

    @Temporal(TemporalType.DATE)
    private Date startDate; // Fecha opcional de inicio de la tarifa

    @Temporal(TemporalType.DATE)
    private Date endDate; // Fecha opcional de fin de la tarifa

    public enum Season {
        HIGH, // Temporada alta
        LOW   // Temporada baja
    }

    public enum RentalDuration {
        DAILY,   // Por día
        WEEKLY,  // Por semana
        MONTHLY  // Por mes
    }
}