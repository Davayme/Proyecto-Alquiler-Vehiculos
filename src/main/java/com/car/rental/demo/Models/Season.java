package com.car.rental.demo.Models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "seasons")
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // Nombre de la temporada (Navidad, Verano, etc.)

    @Column(nullable = false)
    private int startDay; // Día de inicio (sin año)

    @Column(nullable = false)
    private int startMonth; // Mes de inicio

    @Column(nullable = false)
    private int endDay; // Día de fin (sin año)

    @Column(nullable = false)
    private int endMonth; // Mes de fin

    @Builder.Default
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean active = true; // Temporada activa o no

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Rate> rates;
}
