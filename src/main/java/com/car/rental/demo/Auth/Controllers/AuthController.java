package com.car.rental.demo.Auth.Controllers;

import com.car.rental.demo.Auth.Dtos.LoginRequestDto;
import com.car.rental.demo.Auth.Dtos.LoginResponseDto;
import com.car.rental.demo.Auth.Services.AuthService;
import com.google.firebase.auth.FirebaseAuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        try {
            LoginResponseDto response = authService.login(loginRequestDto);
            return ResponseEntity.ok(response);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(401).body("Error de autenticación en Firebase: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Error de solicitud: " + e.getMessage());
        } catch (Exception e) {
            String errorMessage = extractFirebaseErrorMessage(e.getMessage());
            return ResponseEntity.status(500).body("Error interno del servidor: " + errorMessage);
        }
    }

    private String extractFirebaseErrorMessage(String errorMessage) {
        if (errorMessage.contains("INVALID_LOGIN_CREDENTIALS")) {
            return "Credenciales de inicio de sesión inválidas.";
        }
        // Puedes agregar más condiciones para otros mensajes de error específicos de Firebase
        return errorMessage;
    }
}