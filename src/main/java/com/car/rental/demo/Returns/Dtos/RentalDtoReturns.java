package com.car.rental.demo.Returns.Dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalDtoReturns {
    private Long rentalId;
    private String clientIdNumber;
    private Date returnDate;
    private String licensePlate;
}