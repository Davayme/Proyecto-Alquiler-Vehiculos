package com.car.rental.demo.Models;


import jakarta.persistence.*;
import java.util.List;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    private String roleName;
    private String description;

    @OneToMany(mappedBy = "role")
    private List<User> users;

    // Getters and setters
}
