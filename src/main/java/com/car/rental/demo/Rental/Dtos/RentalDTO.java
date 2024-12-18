package com.car.rental.demo.Rental.Dtos;

import com.car.rental.demo.Models.Rate.RentalDuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalDTO {
    private String clientId;
    private Long vehicleId;
    private String employeeId;
    private RentalDuration rentalDuration;
    private int quantityOfDuration;
    private double totalAmount;
}
