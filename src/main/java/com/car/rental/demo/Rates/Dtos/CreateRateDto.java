package com.car.rental.demo.Rates.Dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateRateDto {

    @NotNull(message = "El tipo de vehículo es obligatorio")
    private Long typeId;

    @NotNull(message = "La temporada es obligatoria")
    private Long seasonId;

    @NotNull(message = "La duración del alquiler es obligatoria")
    private String rentalDuration; // DAILY, WEEKLY, MONTHLY

    @Min(value = 0, message = "El costo debe ser mayor o igual a 0")
    private double cost;

    private boolean active = true; // Por defecto, la tarifa está activa
}