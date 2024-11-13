package com.car.rental.demo.Vehicles.Dtos;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleImageDTO {
    @NotBlank(message = "Vehicle is required")
    private Long vehicleId;

    @NotBlank(message = "Image is required")
    private MultipartFile image;
}
