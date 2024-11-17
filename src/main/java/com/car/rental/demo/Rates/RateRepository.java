package com.car.rental.demo.Rates;

import com.car.rental.demo.Models.Rate;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<Rate, Long> {
    boolean existsByType_TypeIdAndSeasonAndRentalDuration(Long typeId, Rate.Season season, Rate.RentalDuration rentalDuration);
    List<Rate> findByType_TypeId(Long typeId);
}