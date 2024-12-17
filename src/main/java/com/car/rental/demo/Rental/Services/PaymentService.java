package com.car.rental.demo.Rental.Services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.car.rental.demo.Clients.Services.ClientService;
import com.car.rental.demo.Models.Client;
import com.car.rental.demo.Models.Payment;
import com.car.rental.demo.Models.Rental;
import com.car.rental.demo.Models.User;
import com.car.rental.demo.Models.Vehicle;
import com.car.rental.demo.Rental.PaymentRepository;
import com.car.rental.demo.Rental.RentalRepository;
import com.car.rental.demo.Rental.ReturnRepository;
import com.car.rental.demo.Rental.Dtos.PaymentDTO;
import com.car.rental.demo.Rental.Dtos.RentalDTO;
import com.car.rental.demo.Users.Services.UserService;
import com.car.rental.demo.Vehicles.Services.VehicleService;



@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private ReturnRepository returnRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private UserService userService;
    @Autowired
    private VehicleService vehicleService;
    public Payment createPayment(PaymentDTO paymentDTO) {
        Rental rental = getRental(paymentDTO.getRentalId()); 

        Payment payment = Payment.builder()
                .amount(paymentDTO.getAmount())
                .paymentDate(new Date())
                .stripePaymentId(paymentDTO.getStripeId())
                .rental(rental)
                .paymentMethod(paymentDTO.getPaymentMethod())
                .build();

        return paymentRepository.save(payment);
    }

    public Payment getPayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
    }

    public void deletePayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        paymentRepository.delete(payment);
    }

    public Rental getRental(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Alquiler no encontrado"));
        return rental;
    }

    public Rental createRental(RentalDTO rentalDTO) {
        Client client = clientService.findByIdNumber(rentalDTO.getClientId());
        User employee = userService.findByUidFirebase(rentalDTO.getEmployeeId()).get();
        Vehicle vehicle = vehicleService.getVehicleById(rentalDTO.getVehicleId()).get();
        Rental rental = Rental.builder()
                .startDate(rentalDTO.getStartDate())
                .endDate(rentalDTO.getEndDate())
                .totalAmount(rentalDTO.getTotalAmount())
                .status(Rental.RentalStatus.RESERVED)
                .client(client)
                .employee(employee)
                .vehicle(vehicle)
                .build();
        return rentalRepository.save(rental);
    }

    public List<Rental> getRentals() {
        return rentalRepository.findAll();
    }

}
