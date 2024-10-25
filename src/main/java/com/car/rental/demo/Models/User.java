package com.car.rental.demo.Models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @ManyToOne
    @JoinColumn(name = "roleId")
    private Role role;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    // Getters and setters
}