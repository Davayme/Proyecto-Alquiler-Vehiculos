package com.car.rental.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Profile("!test")
public class SecurityConfig {

    @Bean
    public FirebaseAuthenticationFilter firebaseAuthenticationFilter() {
        return new FirebaseAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors() // Habilitar soporte de CORS
            .and()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Permitir preflight requests
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.GET, "/vehicles/**").hasAnyRole("ADMIN", "CLIENT") // Permitir GET a CLIENT y ADMIN
                .requestMatchers(HttpMethod.GET, "/type-vehicles/**").hasAnyRole("ADMIN", "CLIENT") // Permitir GET a CLIENT y ADMIN
                .requestMatchers("/vehicles/**").hasRole("ADMIN") // Permitir otros métodos solo a ADMIN
                .requestMatchers("/users/**").hasRole("ADMIN")
                .requestMatchers("/type-vehicles/**").hasRole("ADMIN")
                .requestMatchers("/rates").hasRole("ADMIN")
                .requestMatchers("/clients/**").hasRole("ADMIN")
                .requestMatchers("/rentals/**").hasAnyRole("ADMIN", "CLIENT")
                .requestMatchers("/stripe/**").hasAnyRole("ADMIN", "CLIENT")
                .anyRequest().authenticated() // Requiere autenticación para cualquier otra solicitud
            )
            .addFilterBefore(firebaseAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}