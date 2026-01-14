package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.ClientDto;
import com.ccaBank.feedback.entities.Client;
import com.ccaBank.feedback.entities.Role;
import com.ccaBank.feedback.entities.User;
import com.ccaBank.feedback.repositories.ClientRepository;
import com.ccaBank.feedback.repositories.UserRepository;
import com.ccaBank.feedback.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gestionFA/client")
public class ClientController {

    private final ClientService clientService;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    public ClientController(ClientService clientService, UserRepository userRepository,
                            ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientService = clientService;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<?> registerClient(@Valid @RequestBody ClientDto dto) {

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.ROLE_USER);

        user = userRepository.save(user);

        Client client = new Client();
        client.setFirstName(dto.getFirstName());
        client.setPhone(dto.getPhone());
        client.setVille(dto.getVille());
        client.setNumeroCompte(dto.getNumeroCompte());
        client.setUser(user);

        clientRepository.save(client);

        return ResponseEntity.ok("Client enregistré avec succès");
    }

    @GetMapping
    public List<ClientDto> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    public ClientDto getClientById(@PathVariable Long id) {
        return clientService.findClientById(id);
    }

    @PutMapping("/{id}")
    public ClientDto updateClient(@PathVariable Long id, @RequestBody ClientDto clientDto) {
        return clientService.updateClient(clientDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
    }
}
