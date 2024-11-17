package com.car.rental.demo.Rates.Services;
import com.car.rental.demo.Models.Rate;
import com.car.rental.demo.Models.Season;
import com.car.rental.demo.Models.TypeVehicle;
import com.car.rental.demo.Rates.RateRepository;
import com.car.rental.demo.Rates.SeasonRepository;
import com.car.rental.demo.Rates.Dtos.CreateRateDto;
import com.car.rental.demo.Rates.Dtos.UpdateRateDto;
import com.car.rental.demo.Vehicles.TypeVehicleRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RateService {

    private final RateRepository rateRepository;
    private final TypeVehicleRepository typeVehicleRepository;
    private final SeasonRepository seasonRepository;

    //Obtener tarifas
    public List<Rate> getRates() {
        return rateRepository.findAll().stream()
                .filter(rate -> rate.isActive())
                .collect(Collectors.toList());
    }


    public Rate createRate(CreateRateDto createRateDto) {
        // Validar que el tipo de vehículo exista
        TypeVehicle typeVehicle = typeVehicleRepository.findById(createRateDto.getTypeId())
                .orElseThrow(() -> new EntityNotFoundException("El tipo de vehículo no existe con ID: " + createRateDto.getTypeId()));

        // Validar que la temporada exista
        Season season = seasonRepository.findById(createRateDto.getSeasonId())
                .orElseThrow(() -> new EntityNotFoundException("La temporada no existe con ID: " + createRateDto.getSeasonId()));

        // Validar que no exista una tarifa duplicada
        boolean exists = rateRepository.existsByTypeAndSeasonAndRentalDuration(
                typeVehicle, season, Rate.RentalDuration.valueOf(createRateDto.getRentalDuration())
        );
        if (exists) {
            throw new EntityExistsException("Ya existe una tarifa para este tipo de vehículo, temporada y duración");
        }

        // Crear la nueva tarifa
        Rate rate = Rate.builder()
                .type(typeVehicle)
                .season(season)
                .rentalDuration(Rate.RentalDuration.valueOf(createRateDto.getRentalDuration()))
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