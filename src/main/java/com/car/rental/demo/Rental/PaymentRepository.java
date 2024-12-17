package com.car.rental.demo.Rental;

import org.springframework.stereotype.Repository;

import com.car.rental.demo.Models.Payment;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
