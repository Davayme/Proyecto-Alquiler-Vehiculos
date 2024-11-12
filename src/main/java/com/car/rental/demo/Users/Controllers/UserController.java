package com.car.rental.demo.Users.Controllers;

import com.car.rental.demo.Models.User;
import com.car.rental.demo.Users.Dtos.CreateUserDto;
import com.car.rental.demo.Users.Services.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserDto createUserDto) {
        try {
            User user = userService.createUser(createUserDto);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}