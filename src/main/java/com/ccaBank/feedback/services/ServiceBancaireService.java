package com.ccaBank.feedback.services;

import com.ccaBank.feedback.dtos.ServiceBancaireDto;
import com.ccaBank.feedback.entities.ServiceBancaire;
import com.ccaBank.feedback.exceptions.NosuchExistException;
import com.ccaBank.feedback.repositories.ServiceBancaireRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceBancaireService {

    private final ServiceBancaireRepository serviceBRepository;
    private final ModelMapper modelMapper;

    public ServiceBancaireService(ServiceBancaireRepository serviceBRepository,
                                  ModelMapper modelMapper) {
        this.serviceBRepository = serviceBRepository;
        this.modelMapper = modelMapper;
    }

    public ServiceBancaireDto mapToDto(ServiceBancaire serviceBancaire) {
        ServiceBancaireDto serviceBancaireDto = modelMapper.map(serviceBancaire, ServiceBancaireDto.class);
        return serviceBancaireDto;
    }

    public ServiceBancaire mapToEntity(ServiceBancaireDto serviceBancaireDto) {
        return modelMapper.map(serviceBancaireDto, ServiceBancaire.class);
    }
    public ServiceBancaireDto createServiceBancaire(ServiceBancaireDto serviceBancaireDto) {
        ServiceBancaire serviceBancaire = mapToEntity(serviceBancaireDto);
        return mapToDto(serviceBRepository.save(serviceBancaire));
    }

    public List<ServiceBancaireDto> getAllServiceBancaire() {
        return serviceBRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ServiceBancaireDto findServiceBancaireById(Long id) {
        ServiceBancaire serviceBancaire;
        try {
            serviceBancaire = serviceBRepository.findById(id)
                    .orElseThrow(() -> new NosuchExistException("Service Bancaire introuvable ou inexistant"));
        } catch (RuntimeException e){
            System.err.println("Erreur lors de la recherche :" + e.getMessage());
            throw e;
        }
        return mapToDto(serviceBancaire);
    }

    public ServiceBancaireDto updateServiceBancaire(ServiceBancaireDto serviceBancaireDto,
                                                    Long id) {
        Optional<ServiceBancaire> existingServiceBancaire = serviceBRepository.findById(id);
        if (!existingServiceBancaire.isPresent()) {
            throw new NosuchExistException("Service Bancaire introuvable");
        } else {
            existingServiceBancaire.get().setServiceName(serviceBancaireDto.getServiceName());
            existingServiceBancaire.get().setCategory(serviceBancaireDto.getCategory());
        }
        return mapToDto(serviceBRepository.save(existingServiceBancaire.get()));
    }

    public void deleteServiceBancaire(Long id) {
        Optional<ServiceBancaire> existingServiceBancaire = serviceBRepository.findById(id);
        if (!existingServiceBancaire.isPresent()) {
            throw new NosuchExistException("Service Bancaire introuvable");
        }
        serviceBRepository.deleteById(id);
    }
}
