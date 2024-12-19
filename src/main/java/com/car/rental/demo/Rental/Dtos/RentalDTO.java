package com.car.rental.demo.Rental.Dtos;

import java.util.Date;

import com.car.rental.demo.Models.Rate.RentalDuration;
import com.fasterxml.jackson.annotation.JsonFormat;

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
    private String userId;
    private Date rentalDate;
    private RentalDuration rentalDuration;
    private int quantityOfDuration;
    private double totalAmount;
    
}
