package com.car.rental.demo.Vehicles.Controllers;

import com.car.rental.demo.Cloudinary.CloudinaryService;
import com.car.rental.demo.Exceptions.ResourceNotFoundException;
import com.car.rental.demo.Models.Brand;
import com.car.rental.demo.Models.Model;
import com.car.rental.demo.Models.Vehicle;
import com.car.rental.demo.Models.Vehicle.FuelType;
import com.car.rental.demo.Models.Vehicle.TransmissionType;
import com.car.rental.demo.Models.Vehicle.VehicleStatus;
import com.car.rental.demo.Vehicles.Dtos.VehicleDTO;
import com.car.rental.demo.Vehicles.Dtos.VehicleGet;
import com.car.rental.demo.Vehicles.Services.VehicleService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/vehicles")
@Validated
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private CloudinaryService cloudinaryService;

    // Crear un nuevo vehículo
    @PostMapping
    public ResponseEntity<?> createVehicle(
        @RequestParam @NotBlank(message = "Brand is required") String brand,
        @RequestParam @NotBlank(message = "Model is required") String model,
        @RequestParam @NotBlank(message = "License Plate is required") String licensePlate,
        @RequestParam @NotNull(message = "Type ID is required") Long typeId,
        @RequestParam @NotNull(message = "Status is required") VehicleStatus status,
         @RequestParam @PastOrPresent(message = "Acquisition date must be in the past or present") 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date acquisitionDate,
        @RequestParam @PositiveOrZero(message = "Mileage must be zero or positive") double mileage,
        @RequestParam @NotBlank(message = "Location is required") String location,
        @RequestParam @NotNull(message = "Air Conditioning is required") Boolean airConditioning,
        @RequestParam @NotNull(message = "Number of Doors is required") Integer numberOfDoors,
        @RequestParam @NotNull(message = "Fuel Type is required") FuelType fuelType,
        @RequestParam @NotNull(message = "Transmission Type is required") TransmissionType transmissionType,
        @RequestParam("images") MultipartFile[] images) {
    try {
        VehicleDTO vehicleDTO = VehicleDTO.builder()
        .brand(brand)
        .model(model)
        .licensePlate(licensePlate)
        .typeId(typeId)
        .status(status)
        .acquisitionDate(acquisitionDate)
        .mileage(mileage)
        .location(location)
        .airConditioning(airConditioning)
        .numberOfDoors(numberOfDoors)
        .fuelType(fuelType)
        .transmissionType(transmissionType)
        .build();
            Vehicle vehicle = vehicleService.createVehicle(vehicleDTO);
            for (MultipartFile image : images) {
                String imageUrl = cloudinaryService.uploadImage(image);
                vehicleService.createVehicleImage(vehicle.getVehicleId(), imageUrl);
            }
            Vehicle vehicleAux = vehicleService.getVehicleById(vehicle.getVehicleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID " + vehicle.getVehicleId()));
            return ResponseEntity.status(HttpStatus.CREATED).body(vehicleAux);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el vehículo: " + ex.getMessage());
        }
    }

    // Insertar imagenes
    @PostMapping("/{id}")
    public ResponseEntity<?> createVehicleImage(@PathVariable Long id, @RequestParam("images") MultipartFile[] images) {
        try {
            for (MultipartFile image : images) {
                String imageUrl = cloudinaryService.uploadImage(image);
                vehicleService.createVehicleImage(id, imageUrl);
            }
            Vehicle vehicle = vehicleService.getVehicleById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID " + id));
            return ResponseEntity.status(HttpStatus.CREATED).body(vehicle);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al subir la imagen: " + e.getMessage());
        }
    }

    // Obtener todos los vehículos
    @GetMapping
    public ResponseEntity<List<VehicleGet>> getAllVehicles() {
        List<VehicleGet> vehicles = vehicleService.getAllVehicles();
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
    public ResponseEntity<?> updateVehicle(@PathVariable("id") Long vehicleId,
            @Valid @RequestBody VehicleDTO vehicleDTO) {
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

    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<?> deleteVehicleImage(@PathVariable Long imageId) {
        try {
            vehicleService.deleteVehicleImage(imageId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Imagen eliminada con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/brands")
    public ResponseEntity<List<Brand>> getAllBrands() {
        List<Brand> brands = vehicleService.getAllBrands();
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/models/{id}")
    public ResponseEntity<?> getBrandById(@PathVariable Long id) {
        List<Model> models = vehicleService.getModelsByBrand(id);
        return ResponseEntity.ok(models);
    }
}