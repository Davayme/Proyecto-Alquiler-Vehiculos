package com.car.rental.demo.Vehicles.Services;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.car.rental.demo.Models.Rate;
import com.car.rental.demo.Models.Season;
import com.car.rental.demo.Models.TypeVehicle;
import com.car.rental.demo.Models.Vehicle;
import com.car.rental.demo.Models.VehicleImage;
import com.car.rental.demo.Vehicles.VehicleImageRepository;
import com.car.rental.demo.Vehicles.VehicleRepository;
import com.car.rental.demo.Vehicles.TypeVehicleRepository;
import com.car.rental.demo.Vehicles.Dtos.VehicleDTO;
import com.car.rental.demo.Vehicles.Dtos.VehicleGet;
import com.car.rental.demo.Vehicles.Dtos.VehicleImageGetDto;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private VehicleImageRepository vehicleImageRepository;

    @Autowired
    private TypeVehicleRepository typeRepository;

    // Crear un vehículo
    public Vehicle createVehicle(VehicleDTO vehicleDTO) {
        TypeVehicle type = typeRepository.findById(vehicleDTO.getTypeId())
                .orElseThrow(() -> new RuntimeException("Tipo de vehículo no encontrado"));

        Vehicle vehicle = new Vehicle();
        vehicle.setBrand(vehicleDTO.getBrand());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setLicensePlate(vehicleDTO.getLicensePlate());
        vehicle.setStatus(vehicleDTO.getStatus());
        vehicle.setAcquisitionDate(vehicleDTO.getAcquisitionDate());
        vehicle.setMileage(vehicleDTO.getMileage());
        vehicle.setLocation(vehicleDTO.getLocation());
        vehicle.setType(type);
        vehicle.setAirConditioning(vehicleDTO.getAirConditioning());
        vehicle.setNumberOfDoors(vehicleDTO.getNumberOfDoors());
        vehicle.setFuelType(vehicleDTO.getFuelType());
        vehicle.setTransmissionType(vehicleDTO.getTransmissionType());
        return vehicleRepository.save(vehicle);
    }

    // Obtener todos los vehículos
    public List<VehicleGet> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .filter(Vehicle::isActive)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private VehicleGet convertToDTO(Vehicle vehicle) {
        VehicleGet dto = new VehicleGet();
        dto.setVehicleId(vehicle.getVehicleId());
        dto.setBrand(vehicle.getBrand());
        dto.setModel(vehicle.getModel());
        dto.setLicensePlate(vehicle.getLicensePlate());
        dto.setStatus(vehicle.getStatus());
        dto.setActive(vehicle.isActive());
        dto.setAcquisitionDate(vehicle.getAcquisitionDate());
        dto.setMileage(vehicle.getMileage());
        dto.setLocation(vehicle.getLocation());
        dto.setType(vehicle.getType().getName());
        dto.setAirConditioning(vehicle.isAirConditioning());
        dto.setNumberOfDoors(vehicle.getNumberOfDoors());
        dto.setFuelType(vehicle.getFuelType());
        dto.setTransmissionType(vehicle.getTransmissionType());

        // Calcular la tarifa diaria basada en la temporada actual
        double dailyRate = calculateDailyRate(vehicle);
        dto.setDailyRate(dailyRate);

        // Convertir las imágenes
        if (vehicle.getImages() != null) {
            List<VehicleImageGetDto> imageDTOs = vehicle.getImages().stream()
                    .map(image -> {
                        VehicleImageGetDto imageDTO = new VehicleImageGetDto();
                        imageDTO.setImageId(image.getId());
                        imageDTO.setImageUrl(image.getImageUrl());
                        return imageDTO;
                    })
                    .collect(Collectors.toList());
            dto.setImages(imageDTOs);
        }
        return dto;
    }

    private double calculateDailyRate(Vehicle vehicle) {
        LocalDate currentDate = LocalDate.now();

        // Obtener las tarifas del tipo de vehículo
        List<Rate> rates = vehicle.getType() != null && vehicle.getType().getRates() != null
                ? vehicle.getType().getRates()
                : Collections.emptyList();

        if (rates.isEmpty()) {
            // Si no hay tarifas, retornar 0.0
            return 0.0;
        }

        // Ordenar las temporadas, priorizando las específicas y dejando "NORMAL" al
        // final
        rates = rates.stream()
                .sorted((rate1, rate2) -> {
                    if (rate1.getSeason().getName().equalsIgnoreCase("NORMAL"))
                        return 1;
                    if (rate2.getSeason().getName().equalsIgnoreCase("NORMAL"))
                        return -1;
                    return 0;
                })
                .collect(Collectors.toList());

        // Evaluar cada temporada
        for (Rate rate : rates) {
            Season season = rate.getSeason();
            if (isDateInSeason(currentDate, season)) {
                return rate.getCost();
            }
        }

        // Si ninguna temporada coincide, usar la tarifa asociada a "NORMAL"
        return rates.stream()
                .filter(rate -> rate.getSeason().getName().equalsIgnoreCase("NORMAL"))
                .findFirst()
                .map(Rate::getCost)
                .orElse(0.0); // Tarifa por defecto si no hay tarifa para "NORMAL"
    }

    private boolean isDateInSeason(LocalDate currentDate, Season season) {
        // Manejar la temporada "NORMAL" como caso especial
        if (season.getStartDay() == 0 && season.getStartMonth() == 0 &&
                season.getEndDay() == 0 && season.getEndMonth() == 0) {
            return false; // "NORMAL" no debe coincidir directamente
        }

        int startDay = season.getStartDay();
        int startMonth = season.getStartMonth();
        int endDay = season.getEndDay();
        int endMonth = season.getEndMonth();

        LocalDate startDate = LocalDate.of(
                currentDate.getYear(), startMonth, startDay);
        LocalDate endDate = LocalDate.of(
                currentDate.getYear(), endMonth, endDay);

        // Ajustar años si la temporada cruza el fin de año
        if (endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
            endDate = endDate.plusYears(1);
        }

        // Verificar si la fecha actual está dentro de la temporada
        if ((currentDate.isEqual(startDate) || currentDate.isAfter(startDate)) &&
                (currentDate.isEqual(endDate) || currentDate.isBefore(endDate))) {
            return true;
        }

        return false;
    }

    // Obtener un vehículo por ID
    public Optional<Vehicle> getVehicleById(Long vehicleId) {
        return vehicleRepository.findById(vehicleId);
    }

    // Actualizar un vehículo
    public Vehicle updateVehicle(Long vehicleId, VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        TypeVehicle type = typeRepository.findById(vehicleDTO.getTypeId())
                .orElseThrow(() -> new RuntimeException("Tipo de vehículo no encontrado"));
        vehicle.setBrand(vehicleDTO.getBrand());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setLicensePlate(vehicleDTO.getLicensePlate());
        vehicle.setStatus(vehicleDTO.getStatus());
        vehicle.setAcquisitionDate(vehicleDTO.getAcquisitionDate());
        vehicle.setMileage(vehicleDTO.getMileage());
        vehicle.setLocation(vehicleDTO.getLocation());
        vehicle.setType(type);
        vehicle.setAirConditioning(vehicleDTO.getAirConditioning());
        vehicle.setNumberOfDoors(vehicleDTO.getNumberOfDoors());
        vehicle.setFuelType(vehicleDTO.getFuelType());
        vehicle.setTransmissionType(vehicleDTO.getTransmissionType());

        return vehicleRepository.save(vehicle);
    }

    // Eliminar un vehículo
    public void deleteVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
        vehicle.setActive(false);
        vehicleRepository.save(vehicle);
    }

    // Ingresar imagenes vehiculo
    public VehicleImage createVehicleImage(Long vehicleId, String url) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
        new VehicleImage();
        return vehicleImageRepository.save(VehicleImage.builder()
                .vehicle(vehicle)
                .imageUrl(url)
                .build());
    }

    public void deleteVehicleImage(Long imageId) {
        VehicleImage image = vehicleImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
        vehicleImageRepository.delete(image);
    }
}
