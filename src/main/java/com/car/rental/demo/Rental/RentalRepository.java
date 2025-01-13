package com.car.rental.demo.Rental;

import org.springframework.stereotype.Repository;

import com.car.rental.demo.Models.Client;
import com.car.rental.demo.Models.Rental;
import com.car.rental.demo.Models.Rental.RentalStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByClientIn(List<Client> clients);
    List<Rental> findByStatus(RentalStatus status);

}
