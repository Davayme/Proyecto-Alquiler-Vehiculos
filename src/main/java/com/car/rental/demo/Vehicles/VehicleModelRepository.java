package com.car.rental.demo.Vehicles;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.car.rental.demo.Models.Brand;
import com.car.rental.demo.Models.Model;
@Repository
public interface VehicleModelRepository extends JpaRepository<Model, Long>{
    List<Model> findByBrandId(Brand brandId);
}
