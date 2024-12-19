package com.car.rental.demo.Rental.Dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseRentalDTO {
    private Long rentalId;
    private String clientIdNumber;
    private String vehicleBrand;
    private String vehicleModel;
    private Date rentalDate;
    private Date returnDate;
    private String rentalDuration;
    private int quantityOfDuration;
    private double totalAmount;
    private String status;
}
