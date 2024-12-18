package com.car.rental.demo.Vehicles.Dtos;

import com.car.rental.demo.Models.Vehicle.VehicleStatus;
import com.car.rental.demo.Models.Vehicle.FuelType;
import com.car.rental.demo.Models.Vehicle.TransmissionType;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidVehicleDTO() {
        VehicleDTO vehicleDTO = VehicleDTO.builder()
                .brand("Toyota")
                .model("Corolla")
                .licensePlate("ABC-123")
                .typeId(1L)
                .status(VehicleStatus.AVAILABLE)
                .acquisitionDate(new Date())
                .mileage(15000.0)
                .location("New York")
                .airConditioning(true)
                .numberOfDoors(4)
                .fuelType(FuelType.GASOLINE)
                .transmissionType(TransmissionType.AUTOMATIC)
                .build();

        Set<ConstraintViolation<VehicleDTO>> violations = validator.validate(vehicleDTO);
        assertThat(violations).isEmpty();
    }

    @Test
    void testInvalidVehicleDTO() {
        VehicleDTO vehicleDTO = VehicleDTO.builder()
                .brand("")
                .model("")
                .licensePlate("")
                .typeId(null)
                .status(null)
                .acquisitionDate(new Date(System.currentTimeMillis() + 86400000)) // Fecha futura
                .mileage(-100.0)
                .location("")
                .airConditioning(null)
                .numberOfDoors(null)
                .fuelType(null)
                .transmissionType(null)
                .build();

        Set<ConstraintViolation<VehicleDTO>> violations = validator.validate(vehicleDTO);
        assertThat(violations).hasSize(12); 
    }

   
}