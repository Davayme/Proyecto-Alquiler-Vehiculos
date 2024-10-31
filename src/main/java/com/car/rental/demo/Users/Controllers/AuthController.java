package com.car.rental.demo.Users.Controllers;

import com.car.rental.demo.Users.Dtos.ForgotPasswordRequest;
import com.car.rental.demo.Users.Dtos.UserCreateDTO;

import com.car.rental.demo.Models.User;
import com.car.rental.demo.Users.Services.*;
import com.car.rental.demo.Users.Utils.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // Endpoint para inicio de sesión
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        if (user != null) {
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole().getRoleId()); // Pasa el rol del usuario
            return ResponseEntity.ok("Token: " + token);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    // Endpoint para recuperación de contraseña
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String response = userService.resetPassword(request);
        return ResponseEntity.ok(response);
    }

    // Endpoint para registrar un nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserCreateDTO userDto) {
        if (userService.emailExists(userDto.getEmail())) {
            return ResponseEntity.status(400).body("Email ya registrado");
        } else {
            userService.registerUser(userDto);
            return ResponseEntity.ok("Usuario registrado exitosamente como cliente");
        }
    }
}
