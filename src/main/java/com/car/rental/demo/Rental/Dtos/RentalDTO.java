package com.car.rental.demo.Rental.Dtos;
import java.util.Date;

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
    private String employeeId;
    private Date startDate;
    private Date endDate;
    private double totalAmount;
}
