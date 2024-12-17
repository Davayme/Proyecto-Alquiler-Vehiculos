package com.car.rental.demo.Stripe;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionDTO {
    private Long rentalId;
    private double amount;
    private String successUrl;
    private String cancelUrl;

}
