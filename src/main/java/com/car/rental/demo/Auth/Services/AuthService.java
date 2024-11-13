package com.car.rental.demo.Auth.Services;

import com.car.rental.demo.Auth.Dtos.LoginRequestDto;
import com.car.rental.demo.Auth.Dtos.LoginResponseDto;
import com.car.rental.demo.Models.User;
import com.car.rental.demo.Users.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Value("${firebase.api.key}")
    private String firebaseApiKey;

    public LoginResponseDto login(LoginRequestDto loginRequestDto) throws FirebaseAuthException {
        // Paso 1: Autenticar al usuario en Firebase usando correo y contraseña
        String firebaseToken = authenticateWithFirebase(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        // Paso 2: Decodificar el token para obtener el UID del usuario
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(firebaseToken);
        String uid = decodedToken.getUid();

        // Paso 3: Buscar al usuario en la base de datos para obtener su rol
        User user = userRepository.findByUidFirebase(uid)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado en la base de datos"));

        // Paso 4: Construir el nombre completo
        String fullName = user.getFirstName() + " " + user.getLastName();

        // Paso 5: Devolver el token, el rol, el nombre completo y el correo en el DTO de respuesta
        return new LoginResponseDto("Bearer " + firebaseToken, user.getRole().name(), fullName, user.getEmail());
    }

    private String authenticateWithFirebase(String email, String password) {
        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + firebaseApiKey;

        // Crear el cuerpo de la solicitud
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);
        requestBody.put("returnSecureToken", true);

        // Crear un cliente REST para enviar la solicitud
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(requestBody),
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

        // Extraer el token de la respuesta de Firebase
        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null) {
            return (String) responseBody.get("idToken");
        } else {
            throw new IllegalStateException("No se pudo obtener el token de Firebase");
        }
    }
}