package com.car.rental.demo.Vehicles.Dtos;

import org.springframework.web.multipart.MultipartFile;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleImageDTO {
    @NotNull(message = "Vehicle is required")
    private Long vehicleId;

    @NotNull(message = "Image is required")
    private MultipartFile image;
}
