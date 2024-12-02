package com.car.rental.demo.Users.Dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.car.rental.demo.Models.User.Role;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class CreateUserDtoTest {
    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testCreateUserDto_Valid() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .email("test10@example.com")
                .password("123456")
                .role(Role.CLIENT)
                .build();

        var violations = validator.validate(createUserDto);
        assertEquals(0, violations.size());
    }

    @Test
    void testCreateUserDto_InvalidEmail() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .email("invalid-email")
                .password("123456")
                .role(Role.CLIENT)
                .build();

        var violations = validator.validate(createUserDto);
        assertEquals(1, violations.size());
    }

    @Test
    void testCreateUserDto_BlankPassword() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .email("test10@example.com")
                .password("")
                .role(Role.CLIENT)
                .build();

        var violations = validator.validate(createUserDto);
        assertEquals(2, violations.size());
    }

    @Test
    void testCreateUserDto_ShortPassword() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .email("test10@example.com")
                .password("123")
                .role(Role.CLIENT)
                .build();

        var violations = validator.validate(createUserDto);
        assertEquals(1, violations.size());
    }

    @Test
    void testGetter(){
        CreateUserDto createUserDto = CreateUserDto.builder()
                .email("test10@example.com")
                .password("123456")
                .role(Role.CLIENT)
                .build();
        assertEquals("test10@example.com", createUserDto.getEmail());
        assertEquals("123456", createUserDto.getPassword());
        assertEquals(Role.CLIENT, createUserDto.getRole());
    }

    @Test
    void testSetter(){
        CreateUserDto createUserDto = CreateUserDto.builder()
        .email("test10@example.com")
        .password("123456")
        .role(Role.CLIENT)
        .build();
        createUserDto.setEmail("test11@example.com");
        createUserDto.setPassword("1234567");
        createUserDto.setRole(Role.ADMIN);
        assertEquals("test11@example.com", createUserDto.getEmail());
        assertEquals("1234567", createUserDto.getPassword());
        assertEquals(Role.ADMIN, createUserDto.getRole());
    }
}
