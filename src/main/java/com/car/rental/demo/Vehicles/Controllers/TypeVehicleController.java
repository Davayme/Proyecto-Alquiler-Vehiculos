package com.car.rental.demo.Vehicles.Controllers;

import com.car.rental.demo.Models.TypeVehicle;
import com.car.rental.demo.Vehicles.Dtos.CreateTypeVehicleDto;
import com.car.rental.demo.Vehicles.Dtos.TypeVehicleDTO;
import com.car.rental.demo.Vehicles.Dtos.UpdateTypeVehicleDto;
import com.car.rental.demo.Vehicles.Services.TypeVehicleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/type-vehicles")
@RequiredArgsConstructor
public class TypeVehicleController {

    private final TypeVehicleService typeVehicleService;

    // Crear un nuevo tipo de vehículo
    @PostMapping
    public ResponseEntity<?> createTypeVehicle(@Valid @RequestBody CreateTypeVehicleDto createDto) {
        try {
            TypeVehicle typeVehicle = typeVehicleService.createTypeVehicle(createDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(typeVehicle);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el tipo de vehículo");
        }
    }

    // Obtener todos los tipos de vehículos
    @GetMapping
    public ResponseEntity<List<TypeVehicleDTO>> getAllTypeVehicles() {
        List<TypeVehicleDTO> typeVehicles = typeVehicleService.getAllTypeVehicles();
        return ResponseEntity.ok(typeVehicles);
    }

    // Editar un tipo de vehículo existente
    @PutMapping
    public ResponseEntity<?> updateTypeVehicle(@Valid @RequestBody UpdateTypeVehicleDto updateDto) {
        try {
            TypeVehicle typeVehicle = typeVehicleService.updateTypeVehicle(updateDto);
            return ResponseEntity.ok(typeVehicle);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el tipo de vehículo");
        }
    }

    // Eliminar un tipo de vehículo
    @DeleteMapping("/{typeId}")
    public ResponseEntity<?> deleteTypeVehicle(@PathVariable Long typeId) {
        try {
            typeVehicleService.deleteTypeVehicle(typeId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el tipo de vehículo");
        }
    }
}