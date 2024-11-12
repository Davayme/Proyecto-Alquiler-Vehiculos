package com.car.rental.demo.Auth.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {

    private String bearerToken;
    private String role;
}