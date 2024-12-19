package com.car.rental.demo.Clients;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.car.rental.demo.Models.Client;
import com.car.rental.demo.Models.User;

import java.util.List;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{
    Optional<Client> findByIdNumber(String idNumber);
    List<Client> findByUser(User user);
}
