package com.car.rental.demo.Rates.Services;
import com.car.rental.demo.Models.Rate;
import com.car.rental.demo.Models.TypeVehicle;
import com.car.rental.demo.Rates.RateRepository;
import com.car.rental.demo.Rates.Dtos.CreateRateDto;
import com.car.rental.demo.Rates.Dtos.UpdateRateDto;
import com.car.rental.demo.Vehicles.TypeVehicleRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RateService {

    private final RateRepository rateRepository;
    private final TypeVehicleRepository typeVehicleRepository;

    public Rate createRate(CreateRateDto createRateDto) {
        // Verificar que el tipo de vehículo exista
        TypeVehicle typeVehicle = typeVehicleRepository.findById(createRateDto.getTypeId())
                .orElseThrow(() -> new IllegalArgumentException("El tipo de vehículo no existe con ID: " + createRateDto.getTypeId()));

        // Verificar que no exista una tarifa duplicada
        if (rateRepository.existsByType_TypeIdAndSeasonAndRentalDuration(
                createRateDto.getTypeId(),
                createRateDto.getSeason(),
                createRateDto.getRentalDuration())) {
            throw new IllegalArgumentException("Ya existe una tarifa para este tipo de vehículo, temporada y duración.");
        }

        // Crear y guardar la tarifa
        Rate rate = Rate.builder()
                .type(typeVehicle)
                .season(createRateDto.getSeason())
                .rentalDuration(createRateDto.getRentalDuration())
                .cost(createRateDto.getCost())
                .active(createRateDto.isActive())
                .build();

        return rateRepository.save(rate);
    }

    // Editar tarifa
    public Rate updateRate(UpdateRateDto updateRateDto) {
        // Verificar que la tarifa exista
        Rate rate = rateRepository.findById(updateRateDto.getRateId())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró una tarifa con ID: " + updateRateDto.getRateId()));

        // Actualizar campos
        rate.setCost(updateRateDto.getCost());
        rate.setActive(updateRateDto.isActive());

        return rateRepository.save(rate);
    }
}