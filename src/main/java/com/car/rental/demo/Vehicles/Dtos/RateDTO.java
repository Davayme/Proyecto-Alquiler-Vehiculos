package com.car.rental.demo.Vehicles.Dtos;

import com.google.auto.value.AutoValue.Builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateDTO {
    private Long rateId;
    private String season;
    private String rentalDuration;
    private double cost;
}