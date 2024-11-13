package com.car.rental.demo.Vehicles.Dtos;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Model is required")
    private String model;

    @NotBlank(message = "License Plate is required")
    private String licensePlate;

    @NotBlank(message = "Type is required")
    private String type;

    @NotBlank(message = "Status is required")
    private String status;

    @Positive(message = "Daily rate must be positive")
    private double dailyRate;

    @PastOrPresent(message = "Acquisition date must be in the past or present")
    private Date acquisitionDate;

    @PositiveOrZero(message = "Mileage must be zero or positive")
    private double mileage;

    @NotBlank(message = "Location is required")
    private String location;

}
