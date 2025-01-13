package com.car.rental.demo.Returns.Services;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.car.rental.demo.Models.Payment;
import com.car.rental.demo.Models.Rental;
import com.car.rental.demo.Models.Rental.RentalStatus;
import com.car.rental.demo.Models.Return;
import com.car.rental.demo.Models.ReturnDetail;
import com.car.rental.demo.Models.ReturnDetail.PartStatus;
import com.car.rental.demo.Models.Vehicle;
import com.car.rental.demo.Rental.PaymentRepository;
import com.car.rental.demo.Rental.RentalRepository;
import com.car.rental.demo.Rental.Services.PaymentService;
import com.car.rental.demo.Returns.ReturnRepository;
import com.car.rental.demo.Returns.Dtos.CreateReturnDTO;
import com.car.rental.demo.Returns.Dtos.CreateReturnWithoutDamageDTO;
import com.car.rental.demo.Returns.Dtos.PaymentDTOReturn;
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

    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private PaymentRepository paymentRepository;

    public List<RentalDtoReturns> getAllRentals() {
        List<Rental> rentals = rentalRepository.findAll();
        return rentals.stream()
                .filter(rental -> rental.getStatus() == RentalStatus.IN_PROGRESS)
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
                .lateFee(createReturnWithoutDamageDTO.getLateFee())
                .build();
        returnRepository.save(returnRecord);

        // Cambiar el estado del vehículo a AVAILABLE
        rental.getVehicle().setStatus(Vehicle.VehicleStatus.AVAILABLE);
        vehicleRepository.save(rental.getVehicle());
        paymentService.updateStatusRental(rental.getRentalId(), "COMPLETED");
        return returnRecord;
    }

    public Return createReturnWithDamage(CreateReturnDTO createReturnDTO) {
        Rental rental = rentalRepository.findById(createReturnDTO.getRentalId()).orElseThrow(() -> new NoSuchElementException("Rental not found"));

        // Crear y guardar la devolución primero
        Return returnRecord = Return.builder()
                .rental(rental)
                .returnDate(createReturnDTO.getReturnDate())
                .lateFee(createReturnDTO.getLateFee()) // Usar la tarifa de retraso proporcionada
                .build();
        returnRecord.updateTotalReturnAmount(); // Calcular el monto total de los daños
        returnRepository.save(returnRecord);

        // Asignar el Return a cada ReturnDetail y guardar
        List<ReturnDetail> returnDetails = createReturnDTO.getReturnDetails().stream()
                .map(detailDTO -> ReturnDetail.builder()
                        .returnRecord(returnRecord) // Asignar el Return a cada ReturnDetail
                        .partName(detailDTO.getPartName())
                        .status(PartStatus.valueOf(detailDTO.getStatus().toUpperCase())) // Convertir el estado a enum
                        .damageCost(detailDTO.getDamageCost())
                        .build())
                .collect(Collectors.toList());

        returnRecord.setDetails(returnDetails); // Asignar la lista de detalles a la devolución
        returnRepository.save(returnRecord); // Guardar la devolución con los detalles

        // Cambiar el estado del vehículo a IN_MAINTENANCE si hay daños
        if (returnDetails.stream().anyMatch(detail -> detail.getStatus() == PartStatus.DAMAGED)) {
            rental.getVehicle().setStatus(Vehicle.VehicleStatus.IN_MAINTENANCE);
        } else {
            rental.getVehicle().setStatus(Vehicle.VehicleStatus.AVAILABLE);
        }
        vehicleRepository.save(rental.getVehicle());
        paymentService.updateStatusRental(rental.getRentalId(), "COMPLETED");
        return returnRecord;
    }

    public Payment createPayment(PaymentDTOReturn paymentDTOReturn) {
        Return returnRecord = returnRepository.findById(paymentDTOReturn.getReturnId()).orElseThrow(() -> new NoSuchElementException("Return not found"));
        Payment payment = Payment.builder()
                .returnRecord(returnRecord)
                .stripePaymentId(paymentDTOReturn.getStripeId())
                .paymentMethod(paymentDTOReturn.getPaymentMethod())
                .amount(paymentDTOReturn.getAmount())
                .paymentDate(new Date())
                .typePayment(paymentDTOReturn.getTypePayment())
                .build();
        return paymentRepository.save(payment);
    }
}
