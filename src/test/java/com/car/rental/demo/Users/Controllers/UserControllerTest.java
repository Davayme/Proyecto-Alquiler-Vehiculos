package com.car.rental.demo.Users.Controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.car.rental.demo.Models.User;
import com.car.rental.demo.Users.Dtos.CreateUserDto;
import com.car.rental.demo.Users.Dtos.EditUserDto;
import com.car.rental.demo.Users.Services.UserService;
import com.car.rental.demo.config.TestSecurityConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testCreateUser() throws Exception {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .email("test11@example.com")
                .password("123456")
                .role(User.Role.CLIENT)
                .build();

        User user = User.builder()
                .id(1L)
                .uidFirebase("testUid")
                .email("test12@example.com")
                .role(User.Role.CLIENT)
                .active(true)
                .build();

        when(userService.createUser(any(CreateUserDto.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.uidFirebase").value("testUid"))
                .andExpect(jsonPath("$.email").value("test12@example.com"))
                .andExpect(jsonPath("$.role").value("CLIENT"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void testDeactivateUser() throws Exception {
        User deactivatedUser = User.builder()
                .id(1L)
                .uidFirebase("testUid")
                .email("test10@example.com")
                .role(User.Role.CLIENT)
                .active(false)
                .build();

        when(userService.deletUser(1L)).thenReturn(deactivatedUser);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Usuario con ID 1 ha sido eliminado correctamente."));
    }

    @Test
    void testGetUsers() throws Exception {
        User user = User.builder()
                .id(1L)
                .uidFirebase("testUid")
                .email("test10@example.com")
                .role(User.Role.CLIENT)
                .active(true)
                .build();

        when(userService.getActiveUsers()).thenReturn(Collections.singletonList(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].email").value("test10@example.com"));
    }

    @Test
    void testUpdateUser() throws Exception {
        EditUserDto editUserDto = EditUserDto.builder()
                .role(User.Role.CLIENT)
                .active(true)
                .build();

        User updatedUser = User.builder()
                .id(1L)
                .uidFirebase("testUid")
                .email("test10@example.com")
                .role(User.Role.CLIENT)
                .active(true)
                .build();

        when(userService.updateUser(eq(1L), any(EditUserDto.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(editUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test10@example.com"));
    }
}
