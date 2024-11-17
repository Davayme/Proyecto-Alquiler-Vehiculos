package com.car.rental.demo.Rates;

import org.springframework.data.jpa.repository.JpaRepository;

import com.car.rental.demo.Models.Season;

public interface SeasonRepository extends JpaRepository<Season, Long> {
    
} 