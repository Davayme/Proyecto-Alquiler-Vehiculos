package com.car.rental.demo.Vehicles;

import com.car.rental.demo.Models.Vehicle;
import com.car.rental.demo.Models.Vehicle.FuelType;
import com.car.rental.demo.Models.Vehicle.TransmissionType;
import com.car.rental.demo.Models.Vehicle.VehicleStatus;
import com.car.rental.demo.Vehicles.VehicleRepository;
import com.car.rental.demo.config.TestSecurityConfig;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class VehicleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VehicleRepository vehicleRepository;

    @BeforeAll
    static void loadEnv() {
        Dotenv dotenv = Dotenv.configure()
                .filename(".env.test")
                .load();
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("CLOUDINARY_CLOUD_NAME", dotenv.get("CLOUDINARY_CLOUD_NAME"));
        System.setProperty("CLOUDINARY_API_KEY", dotenv.get("CLOUDINARY_API_KEY"));
        System.setProperty("CLOUDINARY_API_SECRET", dotenv.get("CLOUDINARY_API_SECRET"));
        System.setProperty("FIREBASE_API_KEY", dotenv.get("FIREBASE_API_KEY"));
    }

    @BeforeEach
    void setUp() {
        vehicleRepository.deleteAll();

        Vehicle vehicle1 = Vehicle.builder()
                .brand("Toyota")
                .model("Corolla")
                .licensePlate("ABC-123")
                .status(VehicleStatus.AVAILABLE)
                .acquisitionDate(new Date())
                .mileage(15000.0)
                .location("New York")
                .airConditioning(true)
                .numberOfDoors(4)
                .fuelType(FuelType.GASOLINE)
                .transmissionType(TransmissionType.AUTOMATIC)
                .build();
        vehicleRepository.save(vehicle1);

        Vehicle vehicle2 = Vehicle.builder()
                .brand("Honda")
                .model("Civic")
                .licensePlate("DEF-456")
                .status(VehicleStatus.AVAILABLE)
                .acquisitionDate(new Date())
                .mileage(20000.0)
                .location("Los Angeles")
                .airConditioning(true)
                .numberOfDoors(4)
                .fuelType(FuelType.HYBRID)
                .transmissionType(TransmissionType.MANUAL)
                .build();
        vehicleRepository.save(vehicle2);
    }

    @Test
    void testGetAllVehicles() throws Exception {
        mockMvc.perform(get("/vehicles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brand").value("Toyota"))
                .andExpect(jsonPath("$[1].brand").value("Honda"));
    }
}