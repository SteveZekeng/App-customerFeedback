package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.ClientDto;
import com.ccaBank.feedback.services.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gestionFA/client")
public class ClientController {

    private final ClientService clientService;
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ClientDto addClient (@RequestBody ClientDto clientDto) {
        return clientService.createClient(clientDto);
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
