package com.car.rental.demo.Vehicles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.car.rental.demo.Models.Brand;

@Repository
public interface VehicleBrandRepository extends JpaRepository<Brand, Long>{
}
