package com.car.rental.demo.Vehicles.Services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.car.rental.demo.Models.Vehicle;
import com.car.rental.demo.Vehicles.VehicleRepository;
import com.car.rental.demo.Vehicles.Dtos.VehicleDTO;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    // Crear un vehículo
    public Vehicle createVehicle(VehicleDTO vehicleDTO) {
        Vehicle vehicle = new Vehicle();
        vehicle.setBrand(vehicleDTO.getBrand());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setLicensePlate(vehicleDTO.getLicensePlate());
        vehicle.setType(vehicleDTO.getType());
        vehicle.setStatus(vehicleDTO.getStatus());
        vehicle.setDailyRate(vehicleDTO.getDailyRate());
        vehicle.setAcquisitionDate(vehicleDTO.getAcquisitionDate());
        vehicle.setMileage(vehicleDTO.getMileage());
        vehicle.setLocation(vehicleDTO.getLocation());
        return vehicleRepository.save(vehicle);
    }

    // Obtener todos los vehículos
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll().stream()
            .filter(vehicle -> vehicle.isActive())
            .collect(Collectors.toList());
    }

    // Obtener un vehículo por ID
    public Optional<Vehicle> getVehicleById(Long vehicleId) {
        return vehicleRepository.findById(vehicleId);
    }

    // Actualizar un vehículo
    public Vehicle updateVehicle(Long vehicleId, VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
        vehicle.setBrand(vehicleDTO.getBrand());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setLicensePlate(vehicleDTO.getLicensePlate());
        vehicle.setType(vehicleDTO.getType());
        vehicle.setStatus(vehicleDTO.getStatus());
        vehicle.setDailyRate(vehicleDTO.getDailyRate());
        vehicle.setAcquisitionDate(vehicleDTO.getAcquisitionDate());
        vehicle.setMileage(vehicleDTO.getMileage());
        vehicle.setLocation(vehicleDTO.getLocation());
        return vehicleRepository.save(vehicle);
    }

    // Eliminar un vehículo
    public void deleteVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
        vehicle.setActive(false);
        vehicleRepository.save(vehicle);
    }
}
