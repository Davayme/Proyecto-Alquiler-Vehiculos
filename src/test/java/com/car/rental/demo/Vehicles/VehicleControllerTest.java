package com.car.rental.demo.Vehicles;

import com.car.rental.demo.Cloudinary.CloudinaryService;
import com.car.rental.demo.Models.Vehicle;
import com.car.rental.demo.Models.Vehicle.FuelType;
import com.car.rental.demo.Models.Vehicle.TransmissionType;
import com.car.rental.demo.Models.Vehicle.VehicleStatus;
import com.car.rental.demo.Vehicles.Controllers.VehicleController;
import com.car.rental.demo.Vehicles.Dtos.VehicleDTO;
import com.car.rental.demo.Vehicles.Dtos.VehicleGet;
import com.car.rental.demo.Vehicles.Services.VehicleService;
import com.car.rental.demo.Users.Services.UserService;
import com.car.rental.demo.Models.User;
import com.car.rental.demo.config.TestSecurityConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehicleController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleService vehicleService;

    @MockBean
    private FirebaseAuth firebaseAuth;

    @MockBean
    private UserService userService;

    @MockBean
    private CloudinaryService cloudinaryService;

    private VehicleGet vehicleGet1;
    private VehicleGet vehicleGet2;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        vehicleGet1 = VehicleGet.builder()
                .vehicleId(1L)
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

        vehicleGet2 = VehicleGet.builder()
                .vehicleId(2L)
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

        user = User.builder()
                .uidFirebase("test-uid")
                .role(User.Role.ADMIN)
                .build();
    }

    @Test
    void testCreateVehicle() throws Exception {
        VehicleDTO vehicleDTO = VehicleDTO.builder()
                .brand("Toyota")
                .model("Corolla")
                .licensePlate("ABC-123")
                .typeId(1L)
                .status(VehicleStatus.AVAILABLE)
                .acquisitionDate(new Date())
                .mileage(15000.0)
                .location("New York")
                .airConditioning(true)
                .numberOfDoors(4)
                .fuelType(FuelType.GASOLINE)
                .transmissionType(TransmissionType.AUTOMATIC)
                .build();

        Vehicle vehicle = Vehicle.builder()
                .vehicleId(1L)
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

        when(vehicleService.createVehicle(any(VehicleDTO.class))).thenReturn(vehicle);
        when(cloudinaryService.uploadImage(any(MultipartFile.class))).thenReturn("http://image.url");
        when(vehicleService.getVehicleById(anyLong())).thenReturn(Optional.of(vehicle));

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
                .andExpect(jsonPath("$.model").value("Corolla"))
                .andExpect(jsonPath("$.licensePlate").value("ABC-123"));

        verify(vehicleService, times(1)).createVehicle(any(VehicleDTO.class));
        verify(cloudinaryService, times(1)).uploadImage(any(MultipartFile.class));
        verify(vehicleService, times(1)).createVehicleImage(anyLong(), anyString());
    }

    @Test
    void testCreateVehicleImage() throws Exception {
        Vehicle vehicle = Vehicle.builder()
                .vehicleId(1L)
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

        when(vehicleService.getVehicleById(anyLong())).thenReturn(Optional.of(vehicle));
        when(cloudinaryService.uploadImage(any(MultipartFile.class))).thenReturn("http://image.url");

        MockMultipartFile mockFile = new MockMultipartFile("images", "image.jpg", "image/jpeg", new byte[10]);

        mockMvc.perform(multipart("/vehicles/1")
                .file(mockFile))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand").value("Toyota"))
                .andExpect(jsonPath("$.model").value("Corolla"))
                .andExpect(jsonPath("$.licensePlate").value("ABC-123"));

        verify(vehicleService, times(1)).createVehicleImage(anyLong(), anyString());
        verify(cloudinaryService, times(1)).uploadImage(any(MultipartFile.class));
    }

    @Test
    void testGetAllVehicles() throws Exception {
        List<VehicleGet> vehicles = Arrays.asList(vehicleGet1, vehicleGet2);

        when(vehicleService.getAllVehicles()).thenReturn(vehicles);

        FirebaseToken mockFirebaseToken = mock(FirebaseToken.class);
        when(firebaseAuth.verifyIdToken(anyString())).thenReturn(mockFirebaseToken);
        when(mockFirebaseToken.getUid()).thenReturn("test-uid");
        when(userService.findByUidFirebase(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/vehicles")
                .header("Authorization", "Bearer test-token")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brand").value("Toyota"))
                .andExpect(jsonPath("$[1].brand").value("Honda"));

        verify(vehicleService, times(1)).getAllVehicles();
    }

    @Test
    void testGetVehicleById() throws Exception {
        Vehicle vehicle = Vehicle.builder()
                .vehicleId(1L)
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

        when(vehicleService.getVehicleById(anyLong())).thenReturn(Optional.of(vehicle));

        mockMvc.perform(get("/vehicles/1")
                .header("Authorization", "Bearer test-token")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Toyota"))
                .andExpect(jsonPath("$.model").value("Corolla"))
                .andExpect(jsonPath("$.licensePlate").value("ABC-123"));

        verify(vehicleService, times(1)).getVehicleById(1L);
    }

    @Test
    void testUpdateVehicle() throws Exception {
        VehicleDTO vehicleDTO = VehicleDTO.builder()
                .brand("Toyota")
                .model("Corolla")
                .licensePlate("ABC-123")
                .typeId(1L)
                .status(VehicleStatus.AVAILABLE)
                .acquisitionDate(new Date())
                .mileage(15000.0)
                .location("New York")
                .airConditioning(true)
                .numberOfDoors(4)
                .fuelType(FuelType.GASOLINE)
                .transmissionType(TransmissionType.AUTOMATIC)
                .build();

        Vehicle updatedVehicle = Vehicle.builder()
                .vehicleId(1L)
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

        when(vehicleService.updateVehicle(anyLong(), any(VehicleDTO.class))).thenReturn(updatedVehicle);

        mockMvc.perform(put("/vehicles/1")
                .header("Authorization", "Bearer test-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"brand\": \"Toyota\", \"model\": \"Corolla\", \"licensePlate\": \"ABC-123\", \"typeId\": 1, \"status\": \"AVAILABLE\", \"acquisitionDate\": \"2022-01-01\", \"mileage\": 15000.0, \"location\": \"New York\", \"airConditioning\": true, \"numberOfDoors\": 4, \"fuelType\": \"GASOLINE\", \"transmissionType\": \"AUTOMATIC\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Toyota"))
                .andExpect(jsonPath("$.model").value("Corolla"))
                .andExpect(jsonPath("$.licensePlate").value("ABC-123"));

        verify(vehicleService, times(1)).updateVehicle(anyLong(), any(VehicleDTO.class));
    }

    @Test
    void testDeleteVehicle() throws Exception {
        doNothing().when(vehicleService).deleteVehicle(anyLong());

        mockMvc.perform(delete("/vehicles/1")
                .header("Authorization", "Bearer test-token")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(vehicleService, times(1)).deleteVehicle(1L);
    }

    @Test
    void testDeleteVehicleImage() throws Exception {
        doNothing().when(vehicleService).deleteVehicleImage(anyLong());

        mockMvc.perform(delete("/vehicles/images/1")
                .header("Authorization", "Bearer test-token")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(vehicleService, times(1)).deleteVehicleImage(1L);
    }
}