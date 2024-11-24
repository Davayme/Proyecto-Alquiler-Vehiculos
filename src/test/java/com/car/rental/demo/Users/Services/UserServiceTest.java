package com.car.rental.demo.Users.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.car.rental.demo.Models.User;
import com.car.rental.demo.Users.UserRepository;
import com.car.rental.demo.Users.Dtos.CreateUserDto;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        if (FirebaseApp.getApps().isEmpty()) {
            FileInputStream serviceAccount;
            try {
                serviceAccount = new FileInputStream("src/main/resources/firebase/firebase-service-account.json");
                FirebaseOptions options;

                options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                FirebaseApp.initializeApp(options);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }

    @Test
    void testCreateUser() throws Exception {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .email("test10@example.com")
                .password("123456")
                .role(User.Role.CLIENT)
                .build();
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User createdUser = userService.createUser(createUserDto);

        assertNotNull(createdUser);
        assertNotNull(createdUser.getUidFirebase());
        assertEquals("test10@example.com", createdUser.getEmail());
        assertEquals(User.Role.CLIENT, createdUser.getRole());

    }

    @Test
    void testDeletUser() {

    }

    @Test
    void testFindByUidFirebase() {

    }

    @Test
    void testGetActiveUsers() {

    }

    @Test
    void testGetFirebaseUser() {

    }

    @Test
    void testUpdateUser() {

    }

    @Test
    void testUpdateUserRole() {

    }

    @Test
    void testVerifyTokenAndGetUid() {

    }
}
