package com.car.rental.demo.Vehicles;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.car.rental.demo.Models.Vehicle;

class VehicleTest {

    @Test
    void testVehicleGettersAndSetters() {
        // Crear un objeto Vehicle y configurar sus valores
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(1L);
        vehicle.setBrand("Toyota");
        vehicle.setModel("Corolla");
        vehicle.setLicensePlate("ABC-123");
        vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
        vehicle.setMileage(15000.0);
        vehicle.setLocation("City Center");
        vehicle.setAirConditioning(true);
        vehicle.setNumberOfDoors(4);
        vehicle.setFuelType(Vehicle.FuelType.GASOLINE);
        vehicle.setTransmissionType(Vehicle.TransmissionType.AUTOMATIC);

        // Validar los valores con AssertJ
        assertThat(vehicle.getVehicleId()).isEqualTo(1L);
        assertThat(vehicle.getBrand()).isEqualTo("Toyota");
        assertThat(vehicle.getModel()).isEqualTo("Corolla");
        assertThat(vehicle.getLicensePlate()).isEqualTo("ABC-123");
        assertThat(vehicle.getStatus()).isEqualTo(Vehicle.VehicleStatus.AVAILABLE);
        assertThat(vehicle.getMileage()).isEqualTo(15000.0);
        assertThat(vehicle.getLocation()).isEqualTo("City Center");
        assertThat(vehicle.isAirConditioning()).isTrue();
        assertThat(vehicle.getNumberOfDoors()).isEqualTo(4);
        assertThat(vehicle.getFuelType()).isEqualTo(Vehicle.FuelType.GASOLINE);
        assertThat(vehicle.getTransmissionType()).isEqualTo(Vehicle.TransmissionType.AUTOMATIC);
    }
}
