package com.car.rental.demo.Rental.Services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.car.rental.demo.Clients.Services.ClientService;
import com.car.rental.demo.Models.Client;
import com.car.rental.demo.Models.Payment;
import com.car.rental.demo.Models.Rate;
import com.car.rental.demo.Models.Rate.RentalDuration;
import com.car.rental.demo.Models.Rental.RentalStatus;
import com.car.rental.demo.Models.Vehicle.VehicleStatus;
import com.car.rental.demo.Models.Rental;
import com.car.rental.demo.Models.Season;
import com.car.rental.demo.Models.TypeVehicle;
import com.car.rental.demo.Models.User;
import com.car.rental.demo.Models.Vehicle;
import com.car.rental.demo.Rental.PaymentRepository;
import com.car.rental.demo.Rental.RentalRepository;
import com.car.rental.demo.Rental.Dtos.PaymentDTO;
import com.car.rental.demo.Rental.Dtos.RentalDTO;
import com.car.rental.demo.Rental.Dtos.ResponseRentalDTO;
import com.car.rental.demo.Users.Services.UserService;
import com.car.rental.demo.Vehicles.Services.VehicleService;
import com.google.api.Http;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private com.car.rental.demo.Returns.ReturnRepository returnRepository;
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
                .typePayment(paymentDTO.getTypePayment())
                .build();
        rental.setStatus(RentalStatus.IN_PROGRESS);
        rentalRepository.save(rental);
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

    public Rental createRental(RentalDTO rentalDTO) throws ParseException {
        Client client = clientService.findByIdNumber(rentalDTO.getClientId());
        // User employee =
        // userService.findByUidFirebase(rentalDTO.getEmployeeId()).get();
        Vehicle vehicle = vehicleService.getVehicleById(rentalDTO.getVehicleId()).get();
        if (!vehicle.isActive() || vehicle.getStatus() != VehicleStatus.AVAILABLE){
            throw new RuntimeException("El vehículo no está disponible");
        }
        Date returnDate = new Date();

        // rentalDTO.setRentalDate(new Date("2021-10-10"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(rentalDTO.getRentalDate());
        calendar.add(Calendar.HOUR, 5);
        Date rentalDate = calendar.getTime();
        switch (rentalDTO.getRentalDuration()) {
            case DAILY:
                // pls the number of days
                calendar.add(Calendar.DATE, rentalDTO.getQuantityOfDuration());
                break;
            case WEEKLY:
                // plus the number of weeks
                calendar.add(Calendar.DATE, rentalDTO.getQuantityOfDuration() * 7);
                break;
            case MONTHLY:
                calendar.add(Calendar.MONTH, rentalDTO.getQuantityOfDuration());
                break;
            default:
                break;
        }
        returnDate = calendar.getTime();
        Rental rental = Rental.builder()
                .rentalDuration(rentalDTO.getRentalDuration().toString())
                .quantityOfDuration(rentalDTO.getQuantityOfDuration())
                .rentalDate(rentalDate)
                .returnDate(returnDate)
                .totalAmount(
                        calculateTotalAmount(rentalDTO.getQuantityOfDuration(), vehicle, rentalDTO.getRentalDuration()))
                .status(Rental.RentalStatus.RESERVED)
                .client(client)
                .vehicle(vehicle)
                .build();
        return rentalRepository.save(rental);
    }

    public List<ResponseRentalDTO> getRentals(String email) {

        List<Client> clients = clientService.findByUser(email);
        List<Rental> rentals = rentalRepository.findByClientIn(clients);
        return rentals.stream()
            .map(rental -> new ResponseRentalDTO(
                    rental.getRentalId(),
                    rental.getClient().getIdNumber(),
                    rental.getVehicle().getBrand(),
                    rental.getVehicle().getModel(),
                    rental.getRentalDate(),
                    rental.getReturnDate(),
                    rental.getRentalDuration(),
                    rental.getQuantityOfDuration(),
                    rental.getTotalAmount(),
                    rental.getStatus().name()
            ))
            .collect(Collectors.toList());
    }

    public Rental updateStatusRental(Long rentalId, String status) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Alquiler no encontrado"));
        rental.setStatus(RentalStatus.valueOf(status.toUpperCase()));
        return rentalRepository.save(rental);
    }

    // obtener todas las rentas que hayan sido aprobadas o reservadas
    public List<ResponseRentalDTO> getRentalsActive() {
        List<Rental> rentals = rentalRepository.findByStatus(Rental.RentalStatus.RESERVED);
        rentals.addAll(rentalRepository.findByStatus(Rental.RentalStatus.APPROVED));

        
        return rentals.stream()
        .map(rental -> new ResponseRentalDTO(
                rental.getRentalId(),
                rental.getClient().getIdNumber(),
                rental.getVehicle().getBrand(),
                rental.getVehicle().getModel(),
                rental.getRentalDate(),
                rental.getReturnDate(),
                rental.getRentalDuration(),
                rental.getQuantityOfDuration(),
                rental.getTotalAmount(),
                rental.getStatus().name()
        ))
        .collect(Collectors.toList());

    }

    private double calculateTotalAmount(int quantity, Vehicle vehicle, RentalDuration rentalDuration) {
        TypeVehicle type = vehicle.getType();
        List<Rate> rates = type.getRates();
        List<Rate> auxRates = new ArrayList<>();
        for (Rate rate : rates) {
            if (rate.getRentalDuration() != rentalDuration) {
                continue;
            }
            auxRates.add(rate);
        }
        LocalDate currentDate = LocalDate.now();
        int currentDay = currentDate.getDayOfMonth();
        int currentMonth = currentDate.getMonthValue();
        for (Rate rate : auxRates) {
            if (calculateSeason(rate, currentDay, currentMonth)) {
                return rate.getCost() * quantity;
            }
        }
        return 0;
    }

    private boolean calculateSeason(Rate rate, int day, int month) {
        Season season = rate.getSeason();
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
}
