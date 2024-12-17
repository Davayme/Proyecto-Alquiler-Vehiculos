package com.car.rental.demo.Rental.Dtos;


import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "El ID de la sesión es requerido")
    private String sessionId;
    private String stripeId;

    private String paymentMethod;
    private double amount;
}
