package com.ccaBank.feedback.services;

import com.ccaBank.feedback.dtos.ClientDto;
import com.ccaBank.feedback.entities.Client;
import com.ccaBank.feedback.exceptions.NosuchExistException;
import com.ccaBank.feedback.repositories.ClientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    public ClientService(ClientRepository clientRepository, ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
    }

    public ClientDto mapToDto(Client client) {
        ClientDto clientDto = modelMapper.map(client, ClientDto.class);
        return clientDto;
    }

    public Client mapToEntity(ClientDto clientDto) {
        return modelMapper.map(clientDto, Client.class);
    }

//    public ClientDto createClient(ClientDto clientDto) {
//        Client client = mapToEntity(clientDto);
//        return mapToDto(clientRepository.save(client));
//    }

    public List<ClientDto> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ClientDto findClientById(Long id) {
        Client client;
        try {
            client = clientRepository.findById(id)
                    .orElseThrow(() -> new NosuchExistException("Client introuvable ou inexistant"));
        } catch (RuntimeException e) {
            System.err.println("Erreur lors de la recherche :" + e.getMessage());
            throw e;
        }
        return mapToDto(client);
    }

    public ClientDto updateClient(ClientDto clientDto, Long id) {
        Optional<Client> existingClient = clientRepository.findById(id);
        if (!existingClient.isPresent()) {
           throw new NosuchExistException("Client introuvable ou inexistant");
        } else {
            existingClient.get().setFirstName(clientDto.getFirstName());
            existingClient.get().setPhone(clientDto.getPhone());
            existingClient.get().setEmail(clientDto.getEmail());
        }
        return mapToDto(clientRepository.save(existingClient.get()));
    }

    public void deleteClient(Long id) {
        Optional<Client> existingClient = clientRepository.findById(id);
        if (!existingClient.isPresent()) {
            throw new NosuchExistException("Client introuvable ou inexistant");
        }
        clientRepository.deleteById(id);
    }
}
