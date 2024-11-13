package com.car.rental.demo.Users;

import com.car.rental.demo.Models.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUidFirebase(String uidFirebase);
    List<User> findByActiveTrue();
}