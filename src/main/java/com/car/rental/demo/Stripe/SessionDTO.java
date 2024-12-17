package com.car.rental.demo.Stripe;

import com.car.rental.demo.Models.Payment.TypePayment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionDTO {
    private Long rentalId;
    private double amount;
    private String successUrl;
    private String cancelUrl;
    private TypePayment typePayment;
}
