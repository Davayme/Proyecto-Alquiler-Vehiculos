package com.car.rental.demo.Users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;

import org.junit.jupiter.api.Test;

import com.car.rental.demo.Models.User;

public class UserTest {
    @Test
    void testBuilder() {
        User user = User.builder()
                .id(1L)
                .uidFirebase("testUid")
                .email("test10@example.com")
                .role(User.Role.CLIENT)
                .active(true)
                .build();

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("testUid", user.getUidFirebase());
        assertEquals("test10@example.com", user.getEmail());
        assertEquals(User.Role.CLIENT, user.getRole());
        assertEquals(true, user.isActive());
    }

    @Test
    void testDefaultValues() {
        User user = User.builder()
                .uidFirebase("testUid")
                .email("test10@example.com")
                .build();

        assertNotNull(user);
        assertEquals(User.Role.CLIENT, user.getRole());
        assertEquals(true, user.isActive());
    }

    @Test
    void testOnCreate() {
        User user = new User();
        user.onCreate();

        assertNotNull(user.getCreatedAt());
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();
        user.setId(1L);
        user.setUidFirebase("testUid");
        user.setEmail("test10@example.com");
        user.setRole(User.Role.CLIENT);
        user.setActive(true);
        Date now = new Date();
        user.setCreatedAt(now);

        assertEquals(1L, user.getId());
        assertEquals("testUid", user.getUidFirebase());
        assertEquals("test10@example.com", user.getEmail());
        assertEquals(User.Role.CLIENT, user.getRole());
        assertEquals(true, user.isActive());
        assertEquals(now, user.getCreatedAt());
    }
}
