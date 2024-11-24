package com.car.rental.demo.Users.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.car.rental.demo.Models.User;
import com.car.rental.demo.Users.UserRepository;
import com.car.rental.demo.Users.Dtos.CreateUserDto;
import com.car.rental.demo.Users.Dtos.EditUserDto;
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
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .email("test10@example.com")
                .role(User.Role.CLIENT)
                .active(true)
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User deletedUser = userService.deletUser(userId);

        assertNotNull(deletedUser);
        assertEquals(false, deletedUser.isActive());
    }

    @Test
    void testFindByUidFirebase() {
        String uidFirebase = "testUid";
        User user = User.builder()
                .uidFirebase(uidFirebase)
                .email("test10@example.com")
                .role(User.Role.CLIENT)
                .build();
        when(userRepository.findByUidFirebase(uidFirebase)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByUidFirebase(uidFirebase);

        assertNotNull(foundUser);
        assertEquals(uidFirebase, foundUser.get().getUidFirebase());
    }

    @Test
    void testGetActiveUsers() {
        User user1 = User.builder()
                .email("test1@example.com")
                .role(User.Role.CLIENT)
                .active(true)
                .build();
        User user2 = User.builder()
                .email("test2@example.com")
                .role(User.Role.CLIENT)
                .active(true)
                .build();
        when(userRepository.findByActiveTrue()).thenReturn(Arrays.asList(user1, user2));

        List<User> activeUsers = userService.getActiveUsers();

        assertNotNull(activeUsers);
        assertEquals(2, activeUsers.size());
    }

    @Test
    void testUpdateUser() {
        Long userId = 1L;
        EditUserDto editUserDto = EditUserDto.builder()
                .role(User.Role.ADMIN)
                .active(true)
                .build();
        User user = User.builder()
                .id(userId)
                .email("test10@example.com")
                .role(User.Role.CLIENT)
                .active(false)
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = userService.updateUser(userId, editUserDto);

        assertNotNull(updatedUser);
        assertEquals(User.Role.ADMIN, updatedUser.getRole());
        assertEquals(true, updatedUser.isActive());
    }

    @Test
    void testUpdateUserRole() {
        String uidFirebase = "testUid";
        User.Role newRole = User.Role.ADMIN;
        User user = User.builder()
                .uidFirebase(uidFirebase)
                .email("test10@example.com")
                .role(User.Role.CLIENT)
                .build();
        when(userRepository.findByUidFirebase(uidFirebase)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = userService.updateUserRole(uidFirebase, newRole);

        assertNotNull(updatedUser);
        assertEquals(newRole, updatedUser.getRole());
    }

}
