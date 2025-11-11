package com.ccaBank.feedback.services;

import com.ccaBank.feedback.dtos.PropositionDto;
import com.ccaBank.feedback.entities.Proposition;
import com.ccaBank.feedback.exceptions.NosuchExistException;
import com.ccaBank.feedback.repositories.PropositionRepository;
import com.ccaBank.feedback.repositories.QuestionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropositionService {

    private final PropositionRepository propositionRepository;
    private final QuestionRepository questionRepository;
    private final ModelMapper modelMapper;

    public PropositionService(PropositionRepository propositionRepository,
                              ModelMapper modelMapper,
                              QuestionRepository questionRepository) {
        this.propositionRepository = propositionRepository;
        this.modelMapper = modelMapper;
        this.questionRepository = questionRepository;
    }

    private PropositionDto mapToDto(Proposition proposition){
        PropositionDto propositionDto = modelMapper.map(proposition, PropositionDto.class);
        return propositionDto;
    }

    private Proposition mapToEntity(PropositionDto propositionDto){
        return modelMapper.map(propositionDto, Proposition.class);
    }


    public PropositionDto updateProposition(Long id, PropositionDto propositionDto) {
        Optional<Proposition> existingProposition = propositionRepository.findById(id);
        if (!existingProposition.isPresent()) {
            throw new NosuchExistException("proposition introuvable ou inexistant");
        } else {
            existingProposition.get().setLabel(propositionDto.getLabel());
        }
        return mapToDto(propositionRepository.save(existingProposition.get()));
    }
    public boolean deleteProposition(Long id) {
        if (propositionRepository.existsById(id)) {
            propositionRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
