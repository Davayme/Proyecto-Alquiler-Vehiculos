package com.car.rental.demo.Vehicles;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.car.rental.demo.Models.VehicleImage;


@Repository
public interface VehicleImageRepository extends JpaRepository<VehicleImage, Long>{
    
}
