package com.car.rental.demo.Rental;

import org.springframework.stereotype.Repository;

import com.car.rental.demo.Models.Rental;

import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    
}
