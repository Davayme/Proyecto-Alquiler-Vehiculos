package com.car.rental.demo.Returns;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.car.rental.demo.Models.Return;
@Repository
public interface ReturnRepository extends JpaRepository<Return, Long> {
    
}
