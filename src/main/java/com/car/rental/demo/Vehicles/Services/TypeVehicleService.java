package com.car.rental.demo.Vehicles.Services;

import com.car.rental.demo.Models.TypeVehicle;
import com.car.rental.demo.Vehicles.TypeVehicleRepository;
import com.car.rental.demo.Vehicles.Dtos.CreateTypeVehicleDto;
import com.car.rental.demo.Vehicles.Dtos.TypeVehicleDTO;
import com.car.rental.demo.Vehicles.Dtos.UpdateTypeVehicleDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TypeVehicleService {

    private final TypeVehicleRepository typeVehicleRepository;

    // Crear un nuevo tipo de vehículo
    public TypeVehicle createTypeVehicle(CreateTypeVehicleDto createDto) {
        if (typeVehicleRepository.existsByName(createDto.getName())) {
            throw new IllegalArgumentException("Ya existe un tipo de vehículo con este nombre");
        }

        TypeVehicle typeVehicle = TypeVehicle.builder()
                .name(createDto.getName())
                .description(createDto.getDescription())
                .build();

        return typeVehicleRepository.save(typeVehicle);
    }

    // Obtener todos los tipos de vehículos
     public List<TypeVehicleDTO> getAllTypeVehicles() {
        return typeVehicleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    private TypeVehicleDTO convertToDTO(TypeVehicle typeVehicle) {
        return TypeVehicleDTO.builder()
                .typeId(typeVehicle.getTypeId())
                .name(typeVehicle.getName())
                .description(typeVehicle.getDescription())
                .build();
    }

    // Editar un tipo de vehículo existente
    public TypeVehicle updateTypeVehicle(UpdateTypeVehicleDto updateDto) {
        TypeVehicle typeVehicle = typeVehicleRepository.findById(updateDto.getTypeId())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró un tipo de vehículo con ID: " + updateDto.getTypeId()));

        typeVehicle.setName(updateDto.getName());
        typeVehicle.setDescription(updateDto.getDescription());

        return typeVehicleRepository.save(typeVehicle);
    }

    // Eliminar un tipo de vehículo
    public void deleteTypeVehicle(Long typeId) {
        if (!typeVehicleRepository.existsById(typeId)) {
            throw new IllegalArgumentException("No se encontró un tipo de vehículo con ID: " + typeId);
        }
        typeVehicleRepository.deleteById(typeId);
    }
}
