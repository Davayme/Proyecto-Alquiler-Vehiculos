package com.car.rental.demo.Returns.Services;

import java.util.List;
import java.util.stream.Collectors;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.car.rental.demo.Models.Rental;
import com.car.rental.demo.Rental.RentalRepository;
import com.car.rental.demo.Rental.Dtos.RentalDTO;
import com.car.rental.demo.Returns.ReturnRepository;
import com.car.rental.demo.Returns.Dtos.RentalDtoReturns;

@Service
public class ReturnService {

    @Autowired
    private RentalRepository rentalRepository;

    public List<RentalDtoReturns> getAllRentals() {
        List<Rental> rentals = rentalRepository.findAll();
        return rentals.stream()
                .map(rental -> new RentalDtoReturns(
                        rental.getRentalId(),
                        rental.getClient().getIdNumber(),
                        rental.getReturnDate(),
                        rental.getVehicle().getLicensePlate()))
                .collect(Collectors.toList());
    }
}
