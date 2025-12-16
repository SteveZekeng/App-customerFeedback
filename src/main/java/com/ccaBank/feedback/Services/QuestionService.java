package com.ccaBank.feedback.services;

import com.ccaBank.feedback.dtos.PropositionDto;
import com.ccaBank.feedback.dtos.QuestionDto;
import com.ccaBank.feedback.entities.Proposition;
import com.ccaBank.feedback.entities.Question;
import com.ccaBank.feedback.exceptions.NosuchExistException;
import com.ccaBank.feedback.repositories.PropositionRepository;
import com.ccaBank.feedback.repositories.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final PropositionRepository propositionRepository;
    private final ModelMapper modelMapper;

    public QuestionService(QuestionRepository questionRepository,
                           ModelMapper modelMapper,
                           PropositionRepository propositionRepository) {
        this.questionRepository = questionRepository;
        this.modelMapper = modelMapper;
        this.propositionRepository = propositionRepository;
    }

    private QuestionDto mapToDto(Question question) {
        QuestionDto questionDto = modelMapper.map(question, QuestionDto.class);

        if (question.getProposition() != null && !question.getProposition().isEmpty()) {
            List<PropositionDto> propositionDto = question.getProposition()
                    .stream()
                    .map(proposition -> {
                        PropositionDto dto = new PropositionDto();
                        dto.setLabel(proposition.getLabel());
                        dto.setScore(proposition.getScore());
                        return dto;
                    })
                    .collect(Collectors.toList());
            questionDto.setPropositions(propositionDto);
        }

        return questionDto;
    }

    private Question mapToEntity(QuestionDto questionDto) {
        return modelMapper.map(questionDto, Question.class);
    }

    @Transactional()
    public QuestionDto createQuestion(QuestionDto questionDto) {
        Question question = mapToEntity(questionDto);

        question.setLabelQuestion(questionDto.getLabelQuestion());
        question.setInputType(questionDto.getInputType());
        question.setIndexOrder(questionDto.getIndexOrder());

        if (questionDto.getPropositions() != null && !questionDto.getPropositions().isEmpty()) {
            List<Proposition> propositions = questionDto.getPropositions()
                    .stream().map(propositionDto -> {
                        Proposition proposition = new Proposition();
                        proposition.setLabel(propositionDto.getLabel());
                        proposition.setScore(propositionDto.getScore());
                        proposition.setQuestion(question);
                        return proposition;
                    })
                    .collect(Collectors.toList());
            question.setProposition(propositions);
        }
        return mapToDto(questionRepository.save(question));
    }

    public List<QuestionDto> findAllQuestions(){
        return questionRepository.findAll()
                .stream().map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public QuestionDto findQuestionById(Long id){
        Question question;
        try {
            question = questionRepository.findById(id)
                    .orElseThrow(() -> new NosuchExistException("question introuvable ou inexistant"));
        } catch (NosuchExistException e){
            System.err.println("Erreur lors de la recherche :" + e.getMessage());
            throw e;
        }
        return mapToDto(question);
    }

    public  QuestionDto updateQuestion(Long id, QuestionDto questionDto) {
        Optional<Question> existingQuestion = questionRepository.findById(id);
        if (!existingQuestion.isPresent()) {
            throw new NosuchExistException("question introuvable ou inexistant");
        } else {
            existingQuestion.get().setLabelQuestion(questionDto.getLabelQuestion());
            existingQuestion.get().setInputType(questionDto.getInputType());

        }
        return mapToDto(questionRepository.save(existingQuestion.get()));
    }


    public void deleteQuestion(Long id) {
        Optional<Question> question = questionRepository.findById(id);
        if (!question.isPresent()) {
            throw new NosuchExistException("question introuvable ou inexistant");
        }
        questionRepository.deleteById(id);
        log.info("question deleted successfully");
    }
}
