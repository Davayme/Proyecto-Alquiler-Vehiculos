package com.car.rental.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public FirebaseAuthenticationFilter firebaseAuthenticationFilter() {
        return new FirebaseAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Desactivar CSRF si estás usando solo API
            .authorizeHttpRequests(auth -> auth
             // Permitir acceso sin autenticación a la ruta de login
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                 
                .requestMatchers("/vehicles/**").hasRole("ADMIN")
                
                .requestMatchers("/users/**").hasRole("ADMIN")
                .requestMatchers("/type-vehicles/**").hasRole("ADMIN") 
                .requestMatchers("/rates").hasRole("ADMIN")
                .requestMatchers("/clients/**").hasRole("ADMIN")
                .anyRequest().authenticated()  // Requiere autenticación para cualquier otra solicitud
            )
            .addFilterBefore(firebaseAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}