package com.car.rental.demo.Vehicles;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.car.rental.demo.Models.Vehicle;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
@Transactional
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByTypeTypeId(Long typeId);
}