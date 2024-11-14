package com.car.rental.demo.Vehicles.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateTypeVehicleDto {

    @NotBlank(message = "El nombre del tipo de vehículo es obligatorio")
    @Size(max = 50, message = "El nombre no debe exceder los 50 caracteres")
    private String name;

    @Size(max = 255, message = "La descripción no debe exceder los 255 caracteres")
    private String description;
}