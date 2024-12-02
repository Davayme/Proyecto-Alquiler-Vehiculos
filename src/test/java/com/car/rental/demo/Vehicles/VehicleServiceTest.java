package com.car.rental.demo.Vehicles;

import com.car.rental.demo.Models.Rate;
import com.car.rental.demo.Models.Season;
import com.car.rental.demo.Models.TypeVehicle;
import com.car.rental.demo.Models.Vehicle;
import com.car.rental.demo.Models.VehicleImage;
import com.car.rental.demo.Vehicles.Dtos.VehicleDTO;
import com.car.rental.demo.Vehicles.Dtos.VehicleGet;
import com.car.rental.demo.Vehicles.Services.VehicleService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private TypeVehicleRepository typeVehicleRepository;

    @Mock
    private VehicleImageRepository vehicleImageRepository;

    @InjectMocks
    private VehicleService vehicleService;

    private TypeVehicle type;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Crear instancias de Vehicle
        Vehicle vehicle1 = Vehicle.builder()
                .vehicleId(1L)
                .brand("Toyota")
                .model("Corolla")
                .licensePlate("ABC-123")
                .status(Vehicle.VehicleStatus.AVAILABLE)
                .acquisitionDate(new Date())
                .mileage(15000.0)
                .location("New York")
                .airConditioning(true)
                .numberOfDoors(4)
                .fuelType(Vehicle.FuelType.GASOLINE)
                .transmissionType(Vehicle.TransmissionType.AUTOMATIC)
                .build();

        Vehicle vehicle2 = Vehicle.builder()
                .vehicleId(2L)
                .brand("Honda")
                .model("Civic")
                .licensePlate("DEF-456")
                .status(Vehicle.VehicleStatus.AVAILABLE)
                .acquisitionDate(new Date())
                .mileage(20000.0)
                .location("Los Angeles")
                .airConditioning(true)
                .numberOfDoors(4)
                .fuelType(Vehicle.FuelType.HYBRID)
                .transmissionType(Vehicle.TransmissionType.MANUAL)
                .build();

        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(vehicle1);
        vehicles.add(vehicle2);

        // Crear instancias de Rate
        Season season = Season.builder()
                .id(1L)
                .name("SUMMER")
                .startDay(1)
                .startMonth(6)
                .endDay(31)
                .endMonth(8)
                .active(true)
                .build();

        Rate rate1 = Rate.builder()
                .rateId(1L)
                .type(type)
                .season(season)
                .rentalDuration(Rate.RentalDuration.DAILY)
                .cost(100.0)
                .build();

        Rate rate2 = Rate.builder()
                .rateId(2L)
                .type(type)
                .season(season)
                .rentalDuration(Rate.RentalDuration.WEEKLY)
                .cost(600.0)
                .build();

        List<Rate> rates = new ArrayList<>();
        rates.add(rate1);
        rates.add(rate2);

        // Inicializar TypeVehicle con vehículos y tarifas
        type = TypeVehicle.builder()
                .typeId(1L)
                .name("SUV")
                .description("Sport Utility Vehicle")
                .vehicles(vehicles)
                .rates(rates)
                .build();

        // Asociar los vehículos con el tipo de vehículo
        vehicle1.setType(type);
        vehicle2.setType(type);
    }

    @Test
    void testCreateVehicleSuccess() {
        VehicleDTO vehicleDTO = VehicleDTO.builder()
                .brand("Toyota")
                .model("Corolla")
                .licensePlate("ABC-123")
                .typeId(1L)
                .status(Vehicle.VehicleStatus.AVAILABLE)
                .acquisitionDate(new Date())
                .mileage(15000.0)
                .location("New York")
                .airConditioning(true)
                .numberOfDoors(4)
                .fuelType(Vehicle.FuelType.GASOLINE)
                .transmissionType(Vehicle.TransmissionType.AUTOMATIC)
                .build();

        when(typeVehicleRepository.findById(1L)).thenReturn(Optional.of(type));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Vehicle vehicle = vehicleService.createVehicle(vehicleDTO);

        assertThat(vehicle.getBrand()).isEqualTo("Toyota");
        assertThat(vehicle.getModel()).isEqualTo("Corolla");
        assertThat(vehicle.getLicensePlate()).isEqualTo("ABC-123");
        assertThat(vehicle.getType().getName()).isEqualTo("SUV");
        assertThat(vehicle.isActive()).isTrue();

        verify(typeVehicleRepository, times(1)).findById(1L);
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    void testCreateVehicleTypeNotFound() {
        VehicleDTO vehicleDTO = VehicleDTO.builder()
                .brand("Toyota")
                .model("Corolla")
                .licensePlate("ABC-123")
                .typeId(99L)
                .build();

        when(typeVehicleRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            vehicleService.createVehicle(vehicleDTO);
        });

        assertThat(exception.getMessage()).isEqualTo("Tipo de vehículo no encontrado");

        verify(typeVehicleRepository, times(1)).findById(99L);
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void testCreateVehicleDuplicateLicensePlate() {
        VehicleDTO vehicleDTO = VehicleDTO.builder()
                .brand("Toyota")
                .model("Corolla")
                .licensePlate("ABC-123")
                .typeId(1L)
                .status(Vehicle.VehicleStatus.AVAILABLE)
                .acquisitionDate(new Date())
                .mileage(15000.0)
                .location("New York")
                .airConditioning(true)
                .numberOfDoors(4)
                .fuelType(Vehicle.FuelType.GASOLINE)
                .transmissionType(Vehicle.TransmissionType.AUTOMATIC)
                .build();

        when(typeVehicleRepository.findById(1L)).thenReturn(Optional.of(type));
        when(vehicleRepository.save(any(Vehicle.class))).thenThrow(new RuntimeException("Duplicate entry"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            vehicleService.createVehicle(vehicleDTO);
        });

        assertThat(exception.getMessage()).isEqualTo("Duplicate entry");

        verify(typeVehicleRepository, times(1)).findById(1L);
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    void testGetAllVehicles() {
        Vehicle vehicle1 = Vehicle.builder()
                .vehicleId(1L)
                .brand("Toyota")
                .model("Corolla")
                .licensePlate("ABC-123")
                .status(Vehicle.VehicleStatus.AVAILABLE)
                .acquisitionDate(new Date())
                .mileage(15000.0)
                .location("New York")
                .airConditioning(true)
                .numberOfDoors(4)
                .fuelType(Vehicle.FuelType.GASOLINE)
                .transmissionType(Vehicle.TransmissionType.AUTOMATIC)
                .type(type)
                .active(true)
                .build();

        Vehicle vehicle2 = Vehicle.builder()
                .vehicleId(2L)
                .brand("Honda")
                .model("Civic")
                .licensePlate("DEF-456")
                .status(Vehicle.VehicleStatus.AVAILABLE)
                .acquisitionDate(new Date())
                .mileage(20000.0)
                .location("Los Angeles")
                .airConditioning(true)
                .numberOfDoors(4)
                .fuelType(Vehicle.FuelType.HYBRID)
                .transmissionType(Vehicle.TransmissionType.MANUAL)
                .type(type)
                .active(true)
                .build();

        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle1, vehicle2));

        List<VehicleGet> vehicles = vehicleService.getAllVehicles();

        assertThat(vehicles).hasSize(2);
        assertThat(vehicles.get(0).getBrand()).isEqualTo("Toyota");
        assertThat(vehicles.get(1).getBrand()).isEqualTo("Honda");

        verify(vehicleRepository, times(1)).findAll();
    }

    @Test
    void testGetAllVehiclesEmpty() {
        when(vehicleRepository.findAll()).thenReturn(List.of());

        List<VehicleGet> vehicles = vehicleService.getAllVehicles();

        assertThat(vehicles).isEmpty();

        verify(vehicleRepository, times(1)).findAll();
    }

    @Test
    void testGetVehicleByIdSuccess() {
        Vehicle vehicle = Vehicle.builder()
                .vehicleId(1L)
                .brand("Toyota")
                .model("Corolla")
                .licensePlate("ABC-123")
                .status(Vehicle.VehicleStatus.AVAILABLE)
                .acquisitionDate(new Date())
                .mileage(15000.0)
                .location("New York")
                .airConditioning(true)
                .numberOfDoors(4)
                .fuelType(Vehicle.FuelType.GASOLINE)
                .transmissionType(Vehicle.TransmissionType.AUTOMATIC)
                .type(type)
                .active(true)
                .build();

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        Optional<Vehicle> foundVehicle = vehicleService.getVehicleById(1L);

        assertThat(foundVehicle).isPresent();
        assertThat(foundVehicle.get().getBrand()).isEqualTo("Toyota");

        verify(vehicleRepository, times(1)).findById(1L);
    }

    @Test
    void testGetVehicleByIdNotFound() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Vehicle> foundVehicle = vehicleService.getVehicleById(1L);

        assertThat(foundVehicle).isNotPresent();

        verify(vehicleRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateVehicleSuccess() {
        VehicleDTO vehicleDTO = VehicleDTO.builder()
                .brand("Toyota")
                .model("Corolla")
                .licensePlate("ABC-123")
                .typeId(1L)
                .status(Vehicle.VehicleStatus.AVAILABLE)
                .acquisitionDate(new Date())
                .mileage(15000.0)
                .location("New York")
                .airConditioning(true)
                .numberOfDoors(4)
                .fuelType(Vehicle.FuelType.GASOLINE)
                .transmissionType(Vehicle.TransmissionType.AUTOMATIC)
                .build();

        Vehicle existingVehicle = Vehicle.builder()
                .vehicleId(1L)
                .brand("Toyota")
                .model("Corolla")
                .licensePlate("ABC-123")
                .status(Vehicle.VehicleStatus.AVAILABLE)
                .acquisitionDate(new Date())
                .mileage(15000.0)
                .location("New York")
                .airConditioning(true)
                .numberOfDoors(4)
                .fuelType(Vehicle.FuelType.GASOLINE)
                .transmissionType(Vehicle.TransmissionType.AUTOMATIC)
                .type(type)
                .active(true)
                .build();

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(existingVehicle));
        when(typeVehicleRepository.findById(1L)).thenReturn(Optional.of(type));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Vehicle updatedVehicle = vehicleService.updateVehicle(1L, vehicleDTO);

        assertThat(updatedVehicle.getBrand()).isEqualTo("Toyota");
        assertThat(updatedVehicle.getModel()).isEqualTo("Corolla");
        assertThat(updatedVehicle.getLicensePlate()).isEqualTo("ABC-123");
        assertThat(updatedVehicle.getType().getName()).isEqualTo("SUV");

        verify(vehicleRepository, times(1)).findById(1L);
        verify(typeVehicleRepository, times(1)).findById(1L);
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    void testUpdateVehicleNotFound() {
        VehicleDTO vehicleDTO = VehicleDTO.builder()
                .brand("Toyota")
                .model("Corolla")
                .licensePlate("ABC-123")
                .typeId(1L)
                .status(Vehicle.VehicleStatus.AVAILABLE)
                .acquisitionDate(new Date())
                .mileage(15000.0)
                .location("New York")
                .airConditioning(true)
                .numberOfDoors(4)
                .fuelType(Vehicle.FuelType.GASOLINE)
                .transmissionType(Vehicle.TransmissionType.AUTOMATIC)
                .build();

        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            vehicleService.updateVehicle(1L, vehicleDTO);
        });

        assertThat(exception.getMessage()).isEqualTo("Vehículo no encontrado");

        verify(vehicleRepository, times(1)).findById(1L);
        verify(typeVehicleRepository, never()).findById(anyLong());
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void testUpdateVehicleTypeNotFound() {
        VehicleDTO vehicleDTO = VehicleDTO.builder()
                .brand("Toyota")
                .model("Corolla")
                .licensePlate("ABC-123")
                .typeId(1L)
                .status(Vehicle.VehicleStatus.AVAILABLE)
                .acquisitionDate(new Date())
                .mileage(15000.0)
                .location("New York")
                .airConditioning(true)
                .numberOfDoors(4)
                .fuelType(Vehicle.FuelType.GASOLINE)
                .transmissionType(Vehicle.TransmissionType.AUTOMATIC)
                .build();

        Vehicle existingVehicle = Vehicle.builder()
                .vehicleId(1L)
                .brand("Toyota")
                .model("Corolla")
                .licensePlate("ABC-123")
                .status(Vehicle.VehicleStatus.AVAILABLE)
                .acquisitionDate(new Date())
                .mileage(15000.0)
                .location("New York")
                .airConditioning(true)
                .numberOfDoors(4)
                .fuelType(Vehicle.FuelType.GASOLINE)
                .transmissionType(Vehicle.TransmissionType.AUTOMATIC)
                .type(type)
                .active(true)
                .build();

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(existingVehicle));
        when(typeVehicleRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            vehicleService.updateVehicle(1L, vehicleDTO);
        });

        assertThat(exception.getMessage()).isEqualTo("Tipo de vehículo no encontrado");

        verify(vehicleRepository, times(1)).findById(1L);
        verify(typeVehicleRepository, times(1)).findById(1L);
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void testDeleteVehicleSuccess() {
        Vehicle existingVehicle = Vehicle.builder()
                .vehicleId(1L)
                .brand("Toyota")
                .model("Corolla")
                .licensePlate("ABC-123")
                .status(Vehicle.VehicleStatus.AVAILABLE)
                .acquisitionDate(new Date())
                .mileage(15000.0)
                .location("New York")
                .airConditioning(true)
                .numberOfDoors(4)
                .fuelType(Vehicle.FuelType.GASOLINE)
                .transmissionType(Vehicle.TransmissionType.AUTOMATIC)
                .type(type)
                .active(true)
                .build();

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(existingVehicle));

        vehicleService.deleteVehicle(1L);

        assertThat(existingVehicle.isActive()).isFalse();

        verify(vehicleRepository, times(1)).findById(1L);
        verify(vehicleRepository, times(1)).save(existingVehicle);
    }

    @Test
    void testDeleteVehicleNotFound() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            vehicleService.deleteVehicle(1L);
        });

        assertThat(exception.getMessage()).isEqualTo("Vehículo no encontrado");

        verify(vehicleRepository, times(1)).findById(1L);
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void testCreateVehicleImageSuccess() {
        Vehicle existingVehicle = Vehicle.builder()
                .vehicleId(1L)
                .brand("Toyota")
                .model("Corolla")
                .licensePlate("ABC-123")
                .status(Vehicle.VehicleStatus.AVAILABLE)
                .acquisitionDate(new Date())
                .mileage(15000.0)
                .location("New York")
                .airConditioning(true)
                .numberOfDoors(4)
                .fuelType(Vehicle.FuelType.GASOLINE)
                .transmissionType(Vehicle.TransmissionType.AUTOMATIC)
                .type(type)
                .active(true)
                .build();

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(existingVehicle));
        when(vehicleImageRepository.save(any(VehicleImage.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VehicleImage vehicleImage = vehicleService.createVehicleImage(1L, "http://example.com/image.jpg");

        assertThat(vehicleImage.getImageUrl()).isEqualTo("http://example.com/image.jpg");
        assertThat(vehicleImage.getVehicle().getVehicleId()).isEqualTo(1L);

        verify(vehicleRepository, times(1)).findById(1L);
        verify(vehicleImageRepository, times(1)).save(any(VehicleImage.class));
    }

    @Test
    void testCreateVehicleImageVehicleNotFound() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            vehicleService.createVehicleImage(1L, "http://example.com/image.jpg");
        });

        assertThat(exception.getMessage()).isEqualTo("Vehículo no encontrado");

        verify(vehicleRepository, times(1)).findById(1L);
        verify(vehicleImageRepository, never()).save(any(VehicleImage.class));
    }

    @Test
    void testDeleteVehicleImageSuccess() {
        VehicleImage vehicleImage = VehicleImage.builder()
                .id(1L)
                .imageUrl("http://example.com/image.jpg")
                .build();

        when(vehicleImageRepository.findById(1L)).thenReturn(Optional.of(vehicleImage));

        vehicleService.deleteVehicleImage(1L);

        verify(vehicleImageRepository, times(1)).findById(1L);
        verify(vehicleImageRepository, times(1)).delete(vehicleImage);
    }

    @Test
    void testDeleteVehicleImageNotFound() {
        when(vehicleImageRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            vehicleService.deleteVehicleImage(1L);
        });

        assertThat(exception.getMessage()).isEqualTo("Imagen no encontrada");

        verify(vehicleImageRepository, times(1)).findById(1L);
        verify(vehicleImageRepository, never()).delete(any(VehicleImage.class));
    }
}