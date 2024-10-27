package com.car.rental.demo.Vehicles.Controllers;

import com.car.rental.demo.Exceptions.ResourceNotFoundException;
import com.car.rental.demo.Models.Vehicle;
import com.car.rental.demo.Vehicles.Dtos.VehicleDTO;
import com.car.rental.demo.Vehicles.Services.VehicleService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
@RestController
@RequestMapping("/vehicles")
@Validated
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    // Crear un nuevo vehículo
    @PostMapping
    public ResponseEntity<?> createVehicle(@Valid @RequestBody VehicleDTO vehicleDTO) {
        try {
            Vehicle vehicle = vehicleService.createVehicle(vehicleDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(vehicle);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el vehículo: " + ex.getMessage());
        }
    }

    // Obtener todos los vehículos
    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }

    // Obtener un vehículo por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getVehicleById(@PathVariable("id") Long vehicleId) {
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID " + vehicleId));
        return ResponseEntity.ok(vehicle);
    }

    // Actualizar un vehículo por ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateVehicle(@PathVariable("id") Long vehicleId, @Valid @RequestBody VehicleDTO vehicleDTO) {
        try {
            Vehicle updatedVehicle = vehicleService.updateVehicle(vehicleId, vehicleDTO);
            return ResponseEntity.ok(updatedVehicle);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + ex.getMessage());
        }
    }

    // Eliminar un vehículo por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable("id") Long vehicleId) {
        try {
            vehicleService.deleteVehicle(vehicleId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("Vehicle with ID " + vehicleId + " has been successfully deleted.");
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + ex.getMessage());
        }
    }
}