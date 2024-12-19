package com.car.rental.demo.Clients.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.car.rental.demo.Clients.ClientRepository;
import com.car.rental.demo.Clients.Dtos.ClientDto;
import com.car.rental.demo.Models.Client;
import com.car.rental.demo.Models.User;
import com.car.rental.demo.Users.Services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final UserService userService;

    public Client findByIdNumber(String idNumber) {
        return clientRepository.findByIdNumber(idNumber).orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
    }

    public List<Client> findByUser(String email) {
        User user = userService.getUserByEmail(email);
        return clientRepository.findByUser(user);
    }

    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    public Client createClient(ClientDto clientDto) {
        User user = userService.getUserByEmail(clientDto.getUser());
        Client client = Client.builder()
                .idNumber(clientDto.getIdNumber())
                .firstName(clientDto.getFirstName())
                .lastName(clientDto.getLastName())
                .secondName(clientDto.getSecondName())
                .secondLastName(clientDto.getSecondLastName())
                .email(clientDto.getEmail())
                .phone(clientDto.getPhone())
                .user(user)
                .build();
        return clientRepository.save(client);
    }

    public Client findById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    public Client updateClient(Long id, ClientDto clientDto) {
        Client client = clientRepository.findById(id).get();
        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setSecondName(clientDto.getSecondName());
        client.setSecondLastName(clientDto.getSecondLastName());
        client.setEmail(clientDto.getEmail());
        client.setPhone(clientDto.getPhone());
        return clientRepository.save(client);
    }
}
