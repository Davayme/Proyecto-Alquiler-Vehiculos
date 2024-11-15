package com.car.rental.demo.Rates.Controllers;

import com.car.rental.demo.Models.Rate;
import com.car.rental.demo.Rates.Dtos.CreateRateDto;
import com.car.rental.demo.Rates.Dtos.UpdateRateDto;
import com.car.rental.demo.Rates.Services.RateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/rates")
@RequiredArgsConstructor
public class RateController {

    private final RateService rateService;

    // obtener tarifas
    @GetMapping
    public ResponseEntity<?> getRates() {
        try {
            return ResponseEntity.ok(rateService.getRates());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener las tarifas");
        }
    }

    // Crear tarifa
    @PostMapping
    public ResponseEntity<?> createRate(@Valid @RequestBody CreateRateDto createRateDto) {
        try {
            Rate rate = rateService.createRate(createRateDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(rate);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la tarifa: " + e.getMessage());
        }
    }

    // Editar tarifa
    @PutMapping
    public ResponseEntity<?> updateRate(@Valid @RequestBody UpdateRateDto updateRateDto) {
        try {
            Rate rate = rateService.updateRate(updateRateDto);
            return ResponseEntity.ok(rate);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la tarifa");
        }
    }

    
}