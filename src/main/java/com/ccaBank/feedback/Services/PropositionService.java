package com.ccaBank.feedback.services;

import com.ccaBank.feedback.dtos.PropositionDto;
import com.ccaBank.feedback.entities.Proposition;
import com.ccaBank.feedback.exceptions.NosuchExistException;
import com.ccaBank.feedback.repositories.PropositionRepository;
import com.ccaBank.feedback.repositories.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PropositionService {

    private final PropositionRepository propositionRepository;
    private final ModelMapper modelMapper;

    public PropositionService(PropositionRepository propositionRepository,
                              ModelMapper modelMapper,
                              QuestionRepository questionRepository) {
        this.propositionRepository = propositionRepository;
        this.modelMapper = modelMapper;
    }

    private PropositionDto mapToDto(Proposition proposition){
        PropositionDto propositionDto = modelMapper.map(proposition, PropositionDto.class);
        return propositionDto;
    }

    private Proposition mapToEntity(PropositionDto propositionDto){
        return modelMapper.map(propositionDto, Proposition.class);
    }


    public List<PropositionDto> findAllPropositions() {
        return propositionRepository.findAll()
                .stream().map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public PropositionDto findPropositionById(Long id) {
        Proposition proposition;
        try {
            proposition = propositionRepository.findById(id).
                    orElseThrow(() -> new NosuchExistException("proposition introuvable"));
        } catch (NosuchExistException e){
            System.err.println("Erreur lors de la recherche :" + e.getMessage());
            throw e;
        }
        return mapToDto(proposition);
    }

    public PropositionDto updateProposition(Long id, PropositionDto propositionDto) {
        Optional<Proposition> existingProposition = propositionRepository.findById(id);
        if (!existingProposition.isPresent()) {
            throw new NosuchExistException("proposition introuvable ou inexistant");
        } else {
            existingProposition.get().setLabel(propositionDto.getLabel());
            existingProposition.get().setScore(propositionDto.getScore());
        }
        return mapToDto(propositionRepository.save(existingProposition.get()));
    }

    public void deleteProposition(Long id) {
        Optional<Proposition> existingProposition = propositionRepository.findById(id);
        if (!existingProposition.isPresent()) {
            throw new NosuchExistException("proposition introuvable");
        }
        propositionRepository.deleteById(id);
        log.info("proposition deleted successfully");
    }
}
