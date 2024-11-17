package com.car.rental.demo.Users.Dtos;

import com.car.rental.demo.Models.User.Role;
// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditUserDto {

    // @NotBlank(message = "El nombre es obligatorio")
    // @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    // private String firstName;

    // @NotBlank(message = "El apellido es obligatorio")
    // @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    // private String lastName;

    // @NotBlank(message = "El número de teléfono es obligatorio")
    // @Size(min = 10, max = 10, message = "El número de teléfono debe tener 10 digitos")
    // private String phone;

    private Role role;  

    private Boolean active;
}
