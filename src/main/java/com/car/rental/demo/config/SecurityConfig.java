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
                .requestMatchers("/users/**").hasAnyRole("ADMIN", "CLIENT", "EMPLOYEE") // Permitir otros métodos solo a ADMIN y CLIENT
                .requestMatchers("/type-vehicles/**").hasRole("ADMIN")
                .requestMatchers("/rates").hasAnyRole("ADMIN", "CLIENT", "EMPLOYEE")
                .requestMatchers("/clients/**").hasAnyRole("ADMIN", "CLIENT", "EMPLOYEE")
                .requestMatchers("/rentals/**").hasAnyRole("ADMIN", "CLIENT", "EMPLOYEE")
                .requestMatchers("/stripe/**").hasAnyRole("ADMIN", "CLIENT", "EMPLOYEE")
                .requestMatchers("/returns/**").hasAnyRole("ADMIN", "CLIENT", "EMPLOYEE")
                .anyRequest().authenticated() // Requiere autenticación para cualquier otra solicitud
            )
            .addFilterBefore(firebaseAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}