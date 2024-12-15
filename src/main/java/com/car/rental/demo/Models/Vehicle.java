package com.car.rental.demo.Models;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.javafaker.Faker;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "vehicles")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "vehicleId")
public class Vehicle {
  private static final Faker faker = new Faker();
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long vehicleId;

  private String brand;
  private String model;
  @Column(unique = true, nullable = false)
  private String licensePlate;

  //@Column(unique = true, nullable = false)
  //@NotBlank(message = "Chasis number cannot be blank")
  @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "Invalid chasis number format")
  private String chasisNumber;
  //@Column(unique = true, nullable = false)
  //@NotBlank(message = "Chasis number cannot be blank")
  @Pattern(regexp = "^[A-Z0-9]{8,12}$", message = "Invalid engine number format")
  private String engineNumber;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private VehicleStatus status; 

  @Builder.Default
  private boolean active = true;

  @Temporal(TemporalType.DATE)
  private Date acquisitionDate;

  private double mileage;
  private String location;
  private boolean airConditioning;
  private int numberOfDoors;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private FuelType fuelType; 

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TransmissionType transmissionType;
  @ManyToOne
  @JoinColumn(name = "typeId", nullable = false)
  private TypeVehicle type;

  @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<VehicleImage> images;

  public enum VehicleStatus {
    AVAILABLE, 
    IN_MAINTENANCE, 
    RENTED 
  }

  public enum FuelType {
    GASOLINE, 
    HYBRID, 
    ELECTRIC 
  }

  public enum TransmissionType {
    MANUAL, 
    AUTOMATIC 
  }

  private String generateChasisNumber() {
      return faker.regexify("[A-HJ-NPR-Z0-9]{17}");
  }

  private String generateEngineNumber() {
      return faker.regexify("[A-Z0-9]{8,12}");
  }

  public void setAutoChasis(){
    this.chasisNumber = generateChasisNumber();
  }
  public void setAutoEngine(){
    this.engineNumber = generateEngineNumber();
  }
}