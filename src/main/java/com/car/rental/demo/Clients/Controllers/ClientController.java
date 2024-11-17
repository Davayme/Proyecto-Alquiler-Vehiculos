package com.car.rental.demo.Clients.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.car.rental.demo.Clients.Dtos.ClientDto;
import com.car.rental.demo.Clients.Services.ClientService;
import com.car.rental.demo.Exceptions.ResourceNotFoundException;
import com.car.rental.demo.Models.Client;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody @Valid ClientDto createClientDto) {
        try {
            Client client = clientService.createClient(createClientDto);
            return ResponseEntity.ok(client);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable("id") Long clientId, @Valid @RequestBody ClientDto clientDto) {
        try {
            Client client = clientService.updateClient(clientId, clientDto);
            return ResponseEntity.ok(client);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable("id") Long clientId) {
        try {
            clientService.deleteClient(clientId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("cliente con ese id: " + clientId + " se eliminó correctamente");
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAll();
        return ResponseEntity.ok(clients);
    }

    // Obtener un vehículo por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable("id") Long clientId) {
        try {
            Client client = clientService.findById(clientId);
            return ResponseEntity.ok(client);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<?> getClientByDni(@PathVariable("dni") String dni) {
        if (dni == null) {
            return ResponseEntity.badRequest().body("El DNI no puede ser nulo");
        }
        if (dni.length() != 10 && dni.length() != 13) {
            return ResponseEntity.badRequest().body("El DNI debe tener 10 o 13 caracteres");
        }
        try {
            Client client = clientService.findByIdNumber(dni);
            return ResponseEntity.ok(client);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + ex.getMessage());
        }
    }
    
}
