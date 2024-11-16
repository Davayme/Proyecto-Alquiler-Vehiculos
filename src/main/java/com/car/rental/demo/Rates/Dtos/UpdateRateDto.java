package com.car.rental.demo.Rates.Dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRateDto {

    @NotNull(message = "El ID de la tarifa es obligatorio")
    private Long rateId;

    @NotNull(message = "El costo es obligatorio")
    @Min(value = 0, message = "El costo debe ser mayor o igual a 0")
    private double cost;

    private boolean active; // Permite activar/desactivar la tarifa
}
