/* package com.car.rental.demo.Vehicles;

import com.car.rental.demo.Vehicles.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VehicleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VehicleRepository vehicleRepository;

    @BeforeEach
    void setUp() {
        vehicleRepository.deleteAll();
    }

    @Test
    void testCreateVehicle() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("images", "image.jpg", "image/jpeg", new byte[10]);

        mockMvc.perform(multipart("/vehicles")
                .file(mockFile)
                .param("brand", "Toyota")
                .param("model", "Corolla")
                .param("licensePlate", "ABC-123")
                .param("typeId", "1")
                .param("status", "AVAILABLE")
                .param("acquisitionDate", "2022-01-01")
                .param("mileage", "15000.0")
                .param("location", "New York")
                .param("airConditioning", "true")
                .param("numberOfDoors", "4")
                .param("fuelType", "GASOLINE")
                .param("transmissionType", "AUTOMATIC"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand").value("Toyota"))
                .andExpect(jsonPath("$.model").value("Corolla"));
    }
} */