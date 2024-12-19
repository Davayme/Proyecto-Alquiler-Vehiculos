package com.car.rental.demo.Returns.Dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReturnWithoutDamageDTO {
    private Long rentalId;
    private Date returnDate;
    private double lateFee;
}