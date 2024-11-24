package com.car.rental.demo.Users.Dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.car.rental.demo.Models.User.Role;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class EditUserDtoTest {
    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testEditUserDto_Valid() {
        EditUserDto editUserDto = EditUserDto.builder()
                .role(Role.CLIENT)
                .active(true)
                .build();

        var violations = validator.validate(editUserDto);
        assertEquals(0, violations.size());
    }

    @Test
    void testEditUserDto_NullRole() {
        EditUserDto editUserDto = EditUserDto.builder()
                .role(null)
                .active(true)
                .build();

        var violations = validator.validate(editUserDto);
        assertEquals(0, violations.size()); // No validation constraints on role
    }

    @Test
    void testEditUserDto_NullActive() {
        EditUserDto editUserDto = EditUserDto.builder()
                .role(Role.CLIENT)
                .active(null)
                .build();

        var violations = validator.validate(editUserDto);
        assertEquals(0, violations.size()); // No validation constraints on active
    }

    @Test
    void testGetters() {
        EditUserDto editUserDto = EditUserDto.builder()
                .role(Role.CLIENT)
                .active(true)
                .build();

        assertEquals(Role.CLIENT, editUserDto.getRole());
        assertEquals(true, editUserDto.getActive());
    }

    @Test
    void testSetters() {
        EditUserDto editUserDto = EditUserDto.builder()
                .role(Role.CLIENT)
                .active(true)
                .build();

        editUserDto.setRole(Role.ADMIN);
        editUserDto.setActive(false);

        assertEquals(Role.ADMIN, editUserDto.getRole());
        assertEquals(false, editUserDto.getActive());
    }
}
