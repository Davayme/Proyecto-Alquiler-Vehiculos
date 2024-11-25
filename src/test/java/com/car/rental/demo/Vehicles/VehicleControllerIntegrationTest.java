package com.car.rental.demo.Vehicles;

import com.car.rental.demo.Models.Rate;
import com.car.rental.demo.Models.Season;
import com.car.rental.demo.Models.TypeVehicle;
import com.car.rental.demo.Models.Vehicle;
import com.car.rental.demo.Models.Vehicle.FuelType;
import com.car.rental.demo.Models.Vehicle.TransmissionType;
import com.car.rental.demo.Models.Vehicle.VehicleStatus;
import com.car.rental.demo.Rates.RateRepository;
import com.car.rental.demo.Rates.SeasonRepository;
import com.car.rental.demo.Vehicles.VehicleRepository;
import com.car.rental.demo.config.TestSecurityConfig;
import com.jayway.jsonpath.JsonPath;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@Transactional
class VehicleControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private VehicleRepository vehicleRepository;

        @Autowired
        private TypeVehicleRepository typeVehicleRepository;

        @Autowired
        private SeasonRepository seasonRepository;

        @Autowired
        private RateRepository rateRepository;

        private Long typeVehicleId;

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
                System.out.println("Setting up test data...");
                vehicleRepository.deleteAll();
                typeVehicleRepository.deleteAll();
                seasonRepository.deleteAll();
                rateRepository.deleteAll();

                Season season = Season.builder()
                                .name("Normal")
                                .startDay(1)
                                .startMonth(1)
                                .endDay(31)
                                .endMonth(12)
                                .active(true)
                                .build();
                season = seasonRepository.save(season);

                TypeVehicle typeVehicle = TypeVehicle.builder()
                                .name("Sedan")
                                .description("A comfortable sedan")
                                .build();
                typeVehicle = typeVehicleRepository.save(typeVehicle); // Guardar primero el TypeVehicle
                typeVehicleId = typeVehicle.getTypeId();

                Rate rate = Rate.builder()
                                .type(typeVehicle)
                                .season(season)
                                .rentalDuration(Rate.RentalDuration.DAILY)
                                .cost(100.0)
                                .active(true)
                                .build();
                rate = rateRepository.save(rate);

                typeVehicle.getRates().add(rate);
                typeVehicle = typeVehicleRepository.save(typeVehicle);

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
                                .type(typeVehicle)
                                .build();

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
                                .type(typeVehicle)
                                .build();

                typeVehicle.getVehicles().add(vehicle1);
                typeVehicle.getVehicles().add(vehicle2);

                typeVehicle = typeVehicleRepository.save(typeVehicle);

        }

        @Test
        void testGetAllVehicles() throws Exception {
                mockMvc.perform(get("/vehicles")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[*].brand", containsInAnyOrder("Toyota", "Honda")));
        }

        @Test
        void testCreateVehicle() throws Exception {
                // Cargar la imagen estática desde src/test/resources
                InputStream imageStream1 = getClass().getResourceAsStream("/test-image.jpg");
                MockMultipartFile image1 = new MockMultipartFile("images", "test-image.jpg", "image/jpeg",
                                imageStream1);

                InputStream imageStream2 = getClass().getResourceAsStream("/test-image.jpg");
                MockMultipartFile image2 = new MockMultipartFile("images", "test-image.jpg", "image/jpeg",
                                imageStream2);

                mockMvc.perform(multipart("/vehicles")
                                .file(image1)
                                .file(image2)
                                .param("brand", "Toyota")
                                .param("model", "Corolla")
                                .param("licensePlate", "ABC-765")
                                .param("typeId", typeVehicleId.toString())
                                .param("status", VehicleStatus.AVAILABLE.toString())
                                .param("acquisitionDate", "2022-01-01")
                                .param("mileage", "15000")
                                .param("location", "New York")
                                .param("airConditioning", "true")
                                .param("numberOfDoors", "4")
                                .param("fuelType", FuelType.GASOLINE.toString())
                                .param("transmissionType", TransmissionType.AUTOMATIC.toString())
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.brand").value("Toyota"))
                                .andExpect(jsonPath("$.model").value("Corolla"))
                                .andExpect(jsonPath("$.licensePlate").value("ABC-765"));
        }

        @Test
        void testUpdateVehicle() throws Exception {
                InputStream imageStream1 = getClass().getResourceAsStream("/test-image.jpg");
                MockMultipartFile image1 = new MockMultipartFile("images", "test-image.jpg", "image/jpeg",
                                imageStream1);

                InputStream imageStream2 = getClass().getResourceAsStream("/test-image.jpg");
                MockMultipartFile image2 = new MockMultipartFile("images", "test-image.jpg", "image/jpeg",
                                imageStream2);

                MvcResult result = mockMvc.perform(multipart("/vehicles")
                                .file(image1)
                                .file(image2)
                                .param("brand", "Toyota")
                                .param("model", "Corolla")
                                .param("licensePlate", "ABC-222")
                                .param("typeId", typeVehicleId.toString()) // Asegúrate de que este ID existe en la base
                                                                           // de datos
                                .param("status", VehicleStatus.AVAILABLE.toString())
                                .param("acquisitionDate", "2022-01-01")
                                .param("mileage", "15000")
                                .param("location", "New York")
                                .param("airConditioning", "true")
                                .param("numberOfDoors", "4")
                                .param("fuelType", FuelType.GASOLINE.toString())
                                .param("transmissionType", TransmissionType.AUTOMATIC.toString())
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.brand").value("Toyota"))
                                .andExpect(jsonPath("$.model").value("Corolla"))
                                .andExpect(jsonPath("$.licensePlate").value("ABC-222"))
                                .andReturn();

                // Obtener el ID del vehículo creado
                String responseContent = result.getResponse().getContentAsString();
                Number createdVehicleIdNumber = JsonPath.read(responseContent, "$.vehicleId");
                Long createdVehicleId = createdVehicleIdNumber.longValue();

                // Actualizar el vehículo creado
                String updatedVehicleJson = "{"
                                + "\"brand\": \"Toyota\","
                                + "\"model\": \"Camry\","
                                + "\"licensePlate\": \"XYZ-789\","
                                + "\"typeId\": " + typeVehicleId + ","
                                + "\"status\": \"AVAILABLE\","
                                + "\"acquisitionDate\": \"2022-01-01\","
                                + "\"mileage\": 20000,"
                                + "\"location\": \"San Francisco\","
                                + "\"airConditioning\": true,"
                                + "\"numberOfDoors\": 4,"
                                + "\"fuelType\": \"GASOLINE\","
                                + "\"transmissionType\": \"AUTOMATIC\""
                                + "}";

                mockMvc.perform(put("/vehicles/{id}", createdVehicleId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatedVehicleJson))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.brand").value("Toyota"))
                                .andExpect(jsonPath("$.model").value("Camry"))
                                .andExpect(jsonPath("$.licensePlate").value("XYZ-789"))
                                .andExpect(jsonPath("$.location").value("San Francisco"))
                                .andExpect(jsonPath("$.mileage").value(20000));
        }

        @Test
        void deleteVehicle() throws Exception {
                // Crear un vehículo
                InputStream imageStream1 = getClass().getResourceAsStream("/test-image.jpg");
                MockMultipartFile image1 = new MockMultipartFile("images", "test-image.jpg", "image/jpeg",
                                imageStream1);

                InputStream imageStream2 = getClass().getResourceAsStream("/test-image.jpg");
                MockMultipartFile image2 = new MockMultipartFile("images", "test-image.jpg", "image/jpeg",
                                imageStream2);

                MvcResult result = mockMvc.perform(multipart("/vehicles")
                                .file(image1)
                                .file(image2)
                                .param("brand", "Toyota")
                                .param("model", "Corolla")
                                .param("licensePlate", "ABC-222")
                                .param("typeId", "1") // Asegúrate de que este ID existe en la base de datos
                                .param("status", VehicleStatus.AVAILABLE.toString())
                                .param("acquisitionDate", "2022-01-01")
                                .param("mileage", "15000")
                                .param("location", "New York")
                                .param("airConditioning", "true")
                                .param("numberOfDoors", "4")
                                .param("fuelType", FuelType.GASOLINE.toString())
                                .param("transmissionType", TransmissionType.AUTOMATIC.toString())
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.brand").value("Toyota"))
                                .andExpect(jsonPath("$.model").value("Corolla"))
                                .andExpect(jsonPath("$.licensePlate").value("ABC-222"))
                                .andReturn();

                // Obtener el ID del vehículo creado
                String responseContent = result.getResponse().getContentAsString();
                Number createdVehicleIdNumber = JsonPath.read(responseContent, "$.vehicleId");
                Long createdVehicleId = createdVehicleIdNumber.longValue();

                // Eliminar el vehículo creado
                mockMvc.perform(delete("/vehicles/{id}", createdVehicleId))
                                .andExpect(status().isNoContent());

                // Verificar que el vehículo ha sido eliminado
                mockMvc.perform(get("/vehicles/{id}", createdVehicleId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.active").value(false));
        }

}