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
        // Autenticar al usuario en Firebase usando correo y contraseña
        Map<String, Object> firebaseTokens = authenticateWithFirebase(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        String idToken = (String) firebaseTokens.get("idToken");
        String refreshToken = (String) firebaseTokens.get("refreshToken");

        // Decodificar el token para obtener el UID del usuario
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        String uid = decodedToken.getUid();

        // Buscar al usuario en la base de datos para obtener su rol
        User user = userRepository.findByUidFirebase(uid).orElse(null);
        if (user == null) {
            User userAux = User.builder()
                    .uidFirebase(uid)
                    .email(loginRequestDto.getEmail())
                    .role(User.Role.CLIENT)
                    .active(true)
                    .build();
            user = userRepository.save(userAux);
        }

        // Devolver el token, el rol, el correo y el refreshToken en el DTO de respuesta
        return new LoginResponseDto("Bearer " + idToken, user.getRole().name(), user.getEmail(), refreshToken);
    }

    private Map<String, Object> authenticateWithFirebase(String email, String password) {
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

        // Extraer los tokens de la respuesta de Firebase
        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null) {
            return responseBody;
        } else {
            throw new IllegalStateException("No se pudo obtener el token de Firebase");
        }
    }
}