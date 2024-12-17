package com.car.rental.demo.Rental.Dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    private Long rentalId;
    private String stripeId;

    private String paymentMethod;
    private double amount;
}
