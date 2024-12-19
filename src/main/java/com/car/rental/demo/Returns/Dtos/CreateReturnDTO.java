package com.car.rental.demo.Returns.Dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReturnDTO {
    private Long rentalId;
    private Date returnDate;
    private double lateFee;
    private List<ReturnDetailDTO> returnDetails = new ArrayList<>();
}

