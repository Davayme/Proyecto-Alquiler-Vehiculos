package com.car.rental.demo.Users.Services;

import com.car.rental.demo.Models.Role;
import com.car.rental.demo.Models.User;
import com.car.rental.demo.Users.Dtos.UserCreateDTO;
import com.car.rental.demo.Users.Dtos.UserUpdateDTO;
import com.car.rental.demo.Users.Dtos.ForgotPasswordRequest;
import com.car.rental.demo.Users.Repository.UserRepository;
import com.car.rental.demo.Users.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;

import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository; 

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

// Registrar nuevo usuario como cliente usando UserCreateDto
public void registerUser(UserCreateDTO userDto) {
    User user = new User();
    user.setFirstName(userDto.getFirstName());
    user.setLastName(userDto.getLastName());
    user.setEmail(userDto.getEmail());
    user.setPassword(passwordEncoder.encode(userDto.getPassword())); // Encripta la contraseña
    user.setPhone(userDto.getPhone());
    
    Long roleId = userDto.getRoleId() != null ? userDto.getRoleId() : 2L;

    user.setRegistrationDate(new Date());

    Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
    user.setRole(role);

    userRepository.save(user);
}

// Actualizar un usuario existente con UserUpdateDto
public void updateUser(Long userId, UserUpdateDTO userDto) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    
    user.setFirstName(userDto.getFirstName());
    user.setLastName(userDto.getLastName());
    user.setPhone(userDto.getPhone());

    userRepository.save(user);
}

    // Autenticar usuario por email y contraseña
    public User authenticateUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            // Log para confirmar la existencia del usuario y el hash de la contraseña
            System.out.println("Usuario encontrado: " + user.get().getEmail());
            System.out.println("Contraseña almacenada (hash): " + user.get().getPassword());
            
            if (passwordEncoder.matches(password, user.get().getPassword())) {
                return user.get();
            } else {
                System.out.println("Contraseña no coincide");
            }
        } else {
            System.out.println("Usuario no encontrado");
        }
        return null;
    }

    // Generar token para recuperación de contraseña
    public String resetPassword(ForgotPasswordRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(request.getNewPassword())); // Encripta la nueva contraseña
            userRepository.save(user);
            return "Contraseña actualizada exitosamente.";
        } else {
            return "Email no encontrado.";
        }
    }

    // Verificar si el email ya está registrado
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    // Registrar nuevo usuario (con encriptación de contraseña)
   /*  public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encriptar la contraseña
        userRepository.save(user);
    }*/

    public void registerUserAsClient(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encripta la contraseña
        
        // Asigna el rol de cliente al usuario
        Role clientRole = roleRepository.findById(2L) // 2L representa el ID del rol de cliente
                .orElseThrow(() -> new RuntimeException("Rol de cliente no encontrado"));
        user.setRole(clientRole);
        
        userRepository.save(user);
    }
}