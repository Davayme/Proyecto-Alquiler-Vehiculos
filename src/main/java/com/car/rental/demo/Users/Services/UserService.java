package com.car.rental.demo.Users.Services;


import com.car.rental.demo.Models.User;
import com.car.rental.demo.Users.UserRepository;
import com.car.rental.demo.Users.Dtos.CreateUserDto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(CreateUserDto createUserDto) throws Exception {
        // Paso 1: Crear el usuario en Firebase Authentication sin registrar el número de teléfono
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
            .setEmail(createUserDto.getEmail())
            .setPassword(createUserDto.getPassword())
            .setDisplayName(createUserDto.getFirstName() + " " + createUserDto.getLastName())
            .setDisabled(false);

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

        // Paso 2: Crear el usuario en la base de datos con el número de teléfono incluido
        User user = User.builder()
            .uidFirebase(userRecord.getUid())
            .firstName(createUserDto.getFirstName())
            .lastName(createUserDto.getLastName())
            .email(createUserDto.getEmail())
            .phone(createUserDto.getPhone())  // El número se guarda solo en la base de datos
            .role(createUserDto.getRole())
            .active(true)
            .build();

        return userRepository.save(user);
    }


    public Optional<User> findByUidFirebase(String uidFirebase) {
        return userRepository.findByUidFirebase(uidFirebase);
    }

    public User updateUserRole(String uidFirebase, User.Role newRole) {
        User user = userRepository.findByUidFirebase(uidFirebase)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con UID: " + uidFirebase));
        user.setRole(newRole);
        return userRepository.save(user);
    }

    public String verifyTokenAndGetUid(String idToken) throws Exception {
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        return decodedToken.getUid();
    }

    public UserRecord getFirebaseUser(String uid) throws Exception {
        return FirebaseAuth.getInstance().getUser(uid);
    }
}