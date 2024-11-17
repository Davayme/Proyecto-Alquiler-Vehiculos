package com.car.rental.demo.Rates;

import com.car.rental.demo.Models.Rate;
import com.car.rental.demo.Models.Season;
import com.car.rental.demo.Models.TypeVehicle;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<Rate, Long> {
    boolean existsByTypeAndSeasonAndRentalDuration(TypeVehicle type, Season season, Rate.RentalDuration rentalDuration);
    List<Rate> findByType_TypeId(Long typeId);
}