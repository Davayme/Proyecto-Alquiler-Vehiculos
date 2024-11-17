package com.car.rental.demo.Users.Dtos;


import com.car.rental.demo.Models.User.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.NotNull;
// import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserDto {

    // @NotBlank(message = "El nombre es obligatorio")
    // @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    // private String firstName;

    // @NotBlank(message = "El apellido es obligatorio")
    // @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    // private String lastName;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico debe tener un formato válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
    
    // @NotBlank(message = "El número de teléfono es obligatorio")
    // @Pattern(regexp = "^09\\d{8}$", message = "El número de teléfono debe ser válido y tener exactamente 10 dígitos, comenzando con 09")
    // private String phone;
    
    
    private Role role;
}