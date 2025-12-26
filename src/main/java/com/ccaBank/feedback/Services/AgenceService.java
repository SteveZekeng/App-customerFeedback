package com.ccaBank.feedback.services;

import com.ccaBank.feedback.dtos.AgenceDto;
import com.ccaBank.feedback.entities.Agence;
import com.ccaBank.feedback.exceptions.NosuchExistException;
import com.ccaBank.feedback.repositories.AgenceRepository;
import com.ccaBank.feedback.repositories.StaffRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AgenceService {

    private final AgenceRepository agenceRepository;
    private final ModelMapper modelMapper;

    public AgenceService(AgenceRepository agenceRepository, StaffRepository staffRepository,
                         ModelMapper modelMapper) {
        this.agenceRepository = agenceRepository;
        this.modelMapper = modelMapper;
    }

    private AgenceDto mapToDto(Agence agence) {
        AgenceDto agenceDto = modelMapper.map(agence, AgenceDto.class);
        return agenceDto;
    }

    private Agence mapToEntity(AgenceDto agenceDto) {
        return modelMapper.map(agenceDto, Agence.class);
    }

    public AgenceDto createAgence (AgenceDto agenceDto) {
        Agence agence = mapToEntity(agenceDto);
        return mapToDto(agenceRepository.save(agence));
    }

    public Page<AgenceDto> findAllAgences (Pageable pageable) {
        return agenceRepository.findAll(pageable)
                .map(this::mapToDto);
    }

    public AgenceDto findAgenceById(Long id){
        Agence agence;
        try {
            agence = agenceRepository.findById(id)
                    .orElseThrow(() -> new NosuchExistException("agence introuvable ou inexistant"));
        } catch (NosuchExistException e){
            System.err.println("Erreur lors de la recherche :" + e.getMessage());
            throw e;
        }
        return mapToDto(agence);
    }

    public AgenceDto updateAgence (Long id, AgenceDto agenceDto) {
        Optional<Agence> existingAgence = agenceRepository.findById(id);
        if (!existingAgence.isPresent()) {
            throw new NosuchExistException("agence introuvable ou inexistant");
        } else {
            existingAgence.get().setAgenceCity(agenceDto.getAgenceCity());
            existingAgence.get().setAgenceLocation(agenceDto.getAgenceLocation());
        }
        return mapToDto(agenceRepository.save(existingAgence.get()));
    }

    public void deleteAgence (Long id) {
        Optional<Agence> existingAgence = agenceRepository.findById(id);
        if (!existingAgence.isPresent()) {
            throw new NosuchExistException("agence introuvable ou inexistant");
        }
        agenceRepository.deleteById(id);
    }

    public Double averageScoreByAgence(Long agenceId) {
        Agence agence = agenceRepository.findById(agenceId)
                .orElseThrow(() -> new NosuchExistException("L'agence avec l'id " + agenceId + " n'existe pas"));
        return agence.getAverage();
    }

    public List<AgenceDto> listAgenceOrder() {
        List<Agence> agences = agenceRepository.findAll();
        agences.sort(Comparator.comparing(Agence::getAverage).reversed().thenComparing(Agence::getAgenceLocation));
        return agences.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

}
