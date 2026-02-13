package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.ClientDto;
import com.ccaBank.feedback.dtos.RegisterUserDto;
import com.ccaBank.feedback.repositories.ClientRepository;
import com.ccaBank.feedback.repositories.UserRepository;
import com.ccaBank.feedback.services.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gestionFA/client")
@Tag(name = "Customers", description = "Client REST API")

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

//    @PostMapping
//    public ResponseEntity<?> registerClient(@Valid @RequestBody ClientDto dto) {
//
//        User user = new User();
//        user.setUsername(dto.getUsername());
////        user.setEmail(dto.getEmail());
//        user.setPassword(passwordEncoder.encode(dto.getPassword()));
//        user.setRole(Role.ROLE_CLIENT);
//
//        user = userRepository.save(user);
//
//        Client client = new Client();
//        client.setFirstName(dto.getFirstName());
//        client.setPhone(dto.getPhone());
//        client.setVille(dto.getVille());
//        client.setNumeroCompte(dto.getNumeroCompte());
//        client.setEmail(dto.getEmail());
//        client.setUser(user);
//
//        clientRepository.save(client);
//
//        return ResponseEntity.ok("Client enregistré avec succès");
//    }

    @Operation(summary = "Liste des clients")
    @GetMapping
    public List<ClientDto> getAllClients() {
        return clientService.getAllClients();
    }

    @Operation(summary = "Retourner le detail d'un client")
    @GetMapping("/{id}")
    public ClientDto getClientById(@PathVariable Long id) {
        return clientService.findClientById(id);
    }

    @Operation(summary = "Mise a jour d'un client")
    @PutMapping("/{id}")
    public ClientDto updateClient(@PathVariable Long id, @RequestBody ClientDto clientDto) {
        return clientService.updateClient(clientDto, id);
    }

    @Operation(summary = "Suppression d'un client")
    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
    }


}
