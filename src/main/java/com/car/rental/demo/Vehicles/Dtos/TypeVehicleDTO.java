package com.car.rental.demo.Vehicles.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeVehicleDTO {
    private Long typeId;
    private String name;
    private String description;
}