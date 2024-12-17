package com.car.rental.demo.Stripe;

import com.car.rental.demo.Models.Rental;
import com.car.rental.demo.Models.Vehicle;
import com.car.rental.demo.Models.Payment.TypePayment;
import com.car.rental.demo.Rental.Services.PaymentService;
import com.car.rental.demo.Vehicles.Services.VehicleService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
// import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
// import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {
    @Autowired
    private PaymentService paymentService;
    
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public Session createCheckoutSession(Long rentalId, double amount, String successUrl, String cancelUrl, TypePayment typePayment) throws StripeException {
        Stripe.apiKey = stripeApiKey;
        String tittle= "";
        Rental rental = paymentService.getRental(rentalId);
        Vehicle vehicle = rental.getVehicle();
        if (typePayment == TypePayment.RENTAL) {
            tittle = "Renta";

        } else if (typePayment == TypePayment.RETURN) {
            tittle = "Devolución";
        }
        String vehicleDescription = String.format("Marca: %s    Modelo: %s \nAño: %s    Kilometraje: %.2f \nUbicación: %s   Aire acondicionado: %s \nNúmero de puertas: %d  Tipo de combustible: %s \nTipo de transmisión: %s",
                vehicle.getBrand(), vehicle.getModel(), vehicle.getAcquisitionDate(), vehicle.getMileage(), vehicle.getLocation(),
                vehicle.isAirConditioning() ? "Sí" : "No", vehicle.getNumberOfDoors(), vehicle.getFuelType(), vehicle.getTransmissionType());
        System.out.println(vehicleDescription);
        // Obtener las URLs de las imágenes del vehículo
        List<String> imageUrls = vehicle.getImages().stream()
        .map(image -> image.getImageUrl())
        .collect(Collectors.toList());

        // Configuración de la sesión
        SessionCreateParams params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT) // Modo de pago
            .setSuccessUrl(successUrl)                // URL de éxito
            .setCancelUrl(cancelUrl)                  // URL de cancelación
            .putAllMetadata(Map.of(
                "rentalId", rentalId.toString() // Agrega datos personalizados como rentalId
                , "typePayment", typePayment.toString() // Agrega datos personalizados como typePayment
            ))
            
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)                  // Cantidad (siempre 1 para servicios)
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("usd")       // Moneda
                            .setUnitAmount((long) (amount * 100)) // Monto en centavos
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName("Pago por "+tittle)   // Nombre del producto
                                    .setDescription("Pago por la renta con ID: " + rentalId)
                                    .setDescription(vehicleDescription) // Descripción del vehículo
                                    .addAllImage(imageUrls)
                                    .build()
                            )
                            
                            .build()
                    )
                    
                    .build()
            )
            .build();

        // Crea la sesión
        return Session.create(params);
    }

    public Session getSessionDetails(String sessionId) throws StripeException {
        Stripe.apiKey = stripeApiKey;
        return Session.retrieve(sessionId);
    }
}
