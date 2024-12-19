package com.car.rental.demo.Returns.Services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.car.rental.demo.Models.Rental;
import com.car.rental.demo.Models.Return;
import com.car.rental.demo.Models.ReturnDetail;
import com.car.rental.demo.Models.Vehicle;
import com.car.rental.demo.Rental.RentalRepository;
import com.car.rental.demo.Returns.ReturnRepository;
import com.car.rental.demo.Returns.Dtos.CreateReturnDTO;
import com.car.rental.demo.Returns.Dtos.CreateReturnWithoutDamageDTO;
import com.car.rental.demo.Returns.Dtos.RentalDtoReturns;

import com.car.rental.demo.Vehicles.VehicleRepository;

@Service
public class ReturnService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private ReturnRepository returnRepository;

    @Autowired 
    private VehicleRepository vehicleRepository;

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


    public Return createReturnWithoutDamage(CreateReturnWithoutDamageDTO createReturnWithoutDamageDTO) {
        Rental rental = rentalRepository.findById(createReturnWithoutDamageDTO.getRentalId()).orElseThrow(() -> new RuntimeException("Rental not found"));
        Return returnRecord = Return.builder()
                .rental(rental)
                .returnDate(createReturnWithoutDamageDTO.getReturnDate())
                .totalReturnAmount(0)
                .build();
        returnRepository.save(returnRecord);

        // Cambiar el estado del vehículo a AVAILABLE
        rental.getVehicle().setStatus(Vehicle.VehicleStatus.AVAILABLE);
        vehicleRepository.save(rental.getVehicle());

        return returnRecord;
    }

    public Return createReturnWithDamage(CreateReturnDTO createReturnDTO) {
        Rental rental = rentalRepository.findById(createReturnDTO.getRentalId()).orElseThrow(() -> new RuntimeException("Rental not found"));
        List<ReturnDetail> returnDetails = createReturnDTO.getReturnDetails().stream()
                .map(detailDTO -> ReturnDetail.builder()
                        .returnRecord(null) // Se asignará después de crear el Return
                        .partName(detailDTO.getPartName())
                        .status(detailDTO.getStatus())
                        .damageCost(detailDTO.getDamageCost())
                        .build())
                .collect(Collectors.toList());

        Return returnRecord = Return.builder()
                .rental(rental)
                .returnDate(createReturnDTO.getReturnDate())
                .details(returnDetails)
                .build();
        returnRecord.updateTotalReturnAmount(); // Calcular el monto total de los daños
        returnRepository.save(returnRecord);

        // Asignar el Return a cada ReturnDetail y guardar
        returnDetails.forEach(detail -> {
            detail.setReturnRecord(returnRecord);
            returnRepository.save(returnRecord);
        });

        // Cambiar el estado del vehículo a AVAILABLE
        rental.getVehicle().setStatus(Vehicle.VehicleStatus.IN_MAINTENANCE);
        vehicleRepository.save(rental.getVehicle());

        return returnRecord;
    }
}
