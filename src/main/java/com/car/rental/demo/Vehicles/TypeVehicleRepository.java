package com.car.rental.demo.Vehicles;
import org.springframework.stereotype.Repository;

import com.car.rental.demo.Models.TypeVehicle;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface TypeVehicleRepository extends JpaRepository<TypeVehicle, Long> {
    boolean existsByName(String name);
}
