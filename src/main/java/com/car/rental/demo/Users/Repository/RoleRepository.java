package com.car.rental.demo.Users.Repository;

import com.car.rental.demo.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
