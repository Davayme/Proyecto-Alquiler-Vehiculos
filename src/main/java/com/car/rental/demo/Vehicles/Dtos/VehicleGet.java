package com.car.rental.demo.Vehicles.Dtos;
import java.util.Date;
import java.util.List;

import com.car.rental.demo.Models.Vehicle.VehicleStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleGet {
    private Long vehicleId;
    private String brand;
    private String model;
    private String licensePlate;
    private VehicleStatus status;
    private boolean active;
    private Date acquisitionDate;
    private double mileage;
    private String location;
    private String type; // Solo el nombre del tipo de vehículo
    private double dailyRate;
    private List<VehicleImageGetDto> images;
}
