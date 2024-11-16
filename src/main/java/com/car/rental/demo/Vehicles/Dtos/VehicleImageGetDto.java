package com.car.rental.demo.Vehicles.Dtos;

import com.google.auto.value.AutoValue.Builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleImageGetDto {
    private Long imageId;
    private String imageUrl;
}
