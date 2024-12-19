package com.car.rental.demo.Returns.Dtos;

import com.car.rental.demo.Models.Payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTOReturn {
    private String sessionId;
    private Long returnId;
    private double amount;
    private String paymentMethod;
    private String stripeId;
    private Payment.TypePayment typePayment;
}