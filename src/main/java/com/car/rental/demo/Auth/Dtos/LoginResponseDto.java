package com.car.rental.demo.Auth.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private String role;
    //private String fullName;
    private String email;
}