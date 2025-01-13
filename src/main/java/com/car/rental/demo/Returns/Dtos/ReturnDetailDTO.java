package com.car.rental.demo.Returns.Dtos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnDetailDTO {
    private String partName;
    private String status;
    private double damageCost;
}