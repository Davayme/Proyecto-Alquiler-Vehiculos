package com.car.rental.demo.Vehicles;

import org.springframework.stereotype.Repository;

import com.car.rental.demo.Models.Vehicle;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByTypeTypeId(Long typeId);
}