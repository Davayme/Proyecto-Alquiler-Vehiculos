package com.car.rental.demo.Clients.Dtos;

import com.car.rental.demo.Decorator.ValidEcuadorianId;
import com.car.rental.demo.Decorator.ValidPhoneNumber;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    @ValidEcuadorianId
    private String idNumber;
    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;
    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;
    private String secondName;
    private String secondLastName;
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico debe tener un formato válido")
    private String email;
    @ValidPhoneNumber
    private String phone;
    private String user;
}
