package com.car.rental.demo.Vehicles.Services;

import java.time.LocalDate;
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
import com.car.rental.demo.Rates.SeasonRepository;
import com.car.rental.demo.Vehicles.VehicleImageRepository;
import com.car.rental.demo.Vehicles.VehicleRepository;
import com.car.rental.demo.Vehicles.TypeVehicleRepository;
import com.car.rental.demo.Vehicles.Dtos.RateDTO;
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

    @Autowired
    private SeasonRepository seasonRepository;
    

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
        int currentDay = currentDate.getDayOfMonth();
        int currentMonth = currentDate.getMonthValue();

        for (Rate rate : vehicle.getType().getRates()) {
            Season season = rate.getSeason();
            if (isDateInSeason(currentDay, currentMonth, season)) {
                return rate.getCost();
            }
        }
        return 0; // Default rate if no season matches
    }

    private boolean isDateInSeason(int day, int month, Season season) {
        int startDay = season.getStartDay();
        int startMonth = season.getStartMonth();
        int endDay = season.getEndDay();
        int endMonth = season.getEndMonth();

        if (startMonth < endMonth || (startMonth == endMonth && startDay <= endDay)) {
            // Season within the same year
            return (month > startMonth || (month == startMonth && day >= startDay)) &&
                   (month < endMonth || (month == endMonth && day <= endDay));
        } else {
            // Season spans the end of the year
            return (month > startMonth || (month == startMonth && day >= startDay)) ||
                   (month < endMonth || (month == endMonth && day <= endDay));
        }
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
}
