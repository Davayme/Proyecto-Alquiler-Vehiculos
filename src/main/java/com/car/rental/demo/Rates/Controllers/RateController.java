package com.car.rental.demo.Rates.Controllers;

import com.car.rental.demo.Models.Rate;
import com.car.rental.demo.Rates.Dtos.CreateRateDto;
import com.car.rental.demo.Rates.Dtos.UpdateRateDto;
import com.car.rental.demo.Rates.Services.RateService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @PostMapping
    public ResponseEntity<?> createRate(@Valid @RequestBody CreateRateDto createRateDto) {
        try {
            Rate rate = rateService.createRate(createRateDto);
            return ResponseEntity.ok(rate);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (EntityExistsException ex) {
            return ResponseEntity.status(409).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error interno del servidor: " + ex.getMessage());
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