package com.ccaBank.feedback.services;

import com.ccaBank.feedback.dtos.ResponseDto;
import com.ccaBank.feedback.entities.Feedback;
import com.ccaBank.feedback.entities.Question;
import com.ccaBank.feedback.entities.Response;
import com.ccaBank.feedback.exceptions.NosuchExistException;
import com.ccaBank.feedback.repositories.FeedbackRepository;
import com.ccaBank.feedback.repositories.QuestionRepository;
import com.ccaBank.feedback.repositories.ResponseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final ModelMapper modelMapper;
    private final QuestionRepository questionRepository;
    private final FeedbackRepository feedbackRepository;

    public ResponseService(ResponseRepository responseRepository, ModelMapper modelMapper,
                           QuestionRepository questionRepository,
                           FeedbackRepository feedbackRepository) {
        this.responseRepository = responseRepository;
        this.modelMapper = modelMapper;
        this.questionRepository = questionRepository;
        this.feedbackRepository = feedbackRepository;
    }

    private ResponseDto mapToDto(Response response) {
        ResponseDto responseDto  = modelMapper.map(response, ResponseDto.class);
        return responseDto;
    }

    private Response mapToEntity(ResponseDto responseDto) {
        return modelMapper.map(responseDto, Response.class);
    }

    public ResponseDto createResponse(ResponseDto responseDto) {
        Response response = mapToEntity(responseDto);

        try {
            if (responseDto.getFeedbackId() != null) {
                Feedback feedback = feedbackRepository.findById(responseDto.getFeedbackId()).orElseThrow(() ->
                        new NosuchExistException("feedback introuvable"));
                response.setFeedback(feedback);
            }
            if (responseDto.getQuestionId() != null) {
                Question question = questionRepository.findById(responseDto.getQuestionId()).orElseThrow(() ->
                        new NosuchExistException("question introuvable"));
                response.setQuestion(question);
            }
        } catch (NosuchExistException e) {
            System.err.println("Erreur lors de la creation :" + e.getMessage());
        }
        return mapToDto(responseRepository.save(response));
    }

    public List<ResponseDto> findAllResponses() {
        return responseRepository.findAll()
                .stream().map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ResponseDto findResponseById(Long id) {
        Response response;
        try {
            response = responseRepository.findById(id)
                    .orElseThrow(() -> new NosuchExistException("reponse introuvable ou inexistante"));
        } catch (NosuchExistException e){
            System.err.println("Erreur lors de la recherche :" + e.getMessage());
            throw e;
        }
        return mapToDto(response);
    }

    public boolean deleteResponse(Long id) {
        if (responseRepository.existsById(id)) {
            responseRepository.deleteById(id);
            return true;
        }
        return false;
    }




}
