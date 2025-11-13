package com.ccaBank.feedback.services;

import com.ccaBank.feedback.dtos.FeedbackDto;
import com.ccaBank.feedback.dtos.ResponseDto;
import com.ccaBank.feedback.entities.*;
import com.ccaBank.feedback.exceptions.NosuchExistException;
import com.ccaBank.feedback.repositories.FeedbackRepository;
import com.ccaBank.feedback.repositories.QuestionRepository;
import com.ccaBank.feedback.repositories.ResponseRepository;
import com.ccaBank.feedback.repositories.StaffRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final StaffRepository staffRepository;
    private final ResponseRepository responseRepository;
    private final QuestionRepository questionRepository;
    private final ModelMapper modelMapper;


    public FeedbackService(FeedbackRepository feedbackRepository,
                           StaffRepository staffRepository,
                           ResponseRepository responseRepository,
                           QuestionRepository questionRepository,
                           ModelMapper modelMapper) {
        this.feedbackRepository = feedbackRepository;
        this.staffRepository = staffRepository;
        this.responseRepository = responseRepository;
        this.questionRepository = questionRepository;
        this.modelMapper = modelMapper;
    }

    private FeedbackDto mapToDto(Feedback feedback) {
        FeedbackDto feedbackDto = modelMapper.map(feedback, FeedbackDto.class);
        if (feedback.getStaff() != null) {
            Staff staffDto = feedback.getStaff();
            feedbackDto.setStaff_id(staffDto.getId());
        }

        if (feedback.getResponse() != null &&  !feedback.getResponse().isEmpty()) {
            List<ResponseDto> responseDto = feedback.getResponse()
                    .stream()
                    .map(response -> {
                        ResponseDto dto = new ResponseDto();
                        dto.setValue(response.getValue());
                        dto.setSelectedLabel(response.getSelectedLabel());
                        dto.setFeedback_id(response.getFeedback().getId());
                        dto.setQuestion_id(response.getQuestion().getId());
                        return dto;
                    })
                    .collect(Collectors.toList());
            feedbackDto.setResponses(responseDto);
        }
        return feedbackDto;
    }

    private Feedback mapToEntity(FeedbackDto feedbackDto) {
        return modelMapper.map(feedbackDto, Feedback.class);
    }

    @Transactional
    public FeedbackDto createFeedback(FeedbackDto feedbackDto) {
        Feedback feedback = mapToEntity(feedbackDto);

        feedback.setCustomerName(feedbackDto.getCustomerName());
        feedback.setCustomerPhone(feedbackDto.getCustomerPhone());
        feedback.setComment(feedbackDto.getComment());

            if (feedbackDto.getStaff_id() != null) {
                Staff staff = staffRepository.findById(feedbackDto.getStaff_id()).orElseThrow(() ->
                        new NosuchExistException("staff introuvable"));
                feedback.setStaff(staff);
            }

            if (feedbackDto.getResponses() != null &&  !feedbackDto.getResponses().isEmpty()) {
                List<Response> responses= feedbackDto.getResponses()
                        .stream().map(responseDto -> {
                            Response response = new Response();
                            response.setValue(responseDto.getValue());
                            response.setSelectedLabel(responseDto.getSelectedLabel());
                            response.setFeedback(feedback);

                            Question question = questionRepository.findById(responseDto.getQuestion_id())
                                    .orElseThrow(() -> new NosuchExistException("question introuvable avec id " ));
                            response.setQuestion(question);

                            if (responseDto.getSelectedLabel() != null) {
                                Optional<Proposition> proposition = question.getProposition()
                                        .stream()
                                        .filter(p ->
                                                p.getLabel().equalsIgnoreCase(responseDto.getSelectedLabel()))
                                        .findFirst();

                                if (proposition.isPresent()) {
                                    response.setSelectedLabel(proposition.get().getLabel());
                                    response.setValue(proposition.get().getScore());
                                } else {
                                    throw new NosuchExistException("proposition introuvable pour la question " + question.getLabelQuestion());
                                }
                            }

                            else {
                                response.setSelectedLabel(null);
                                response.setValue(responseDto.getValue());
                            }

                            return response;
                        })
                        .collect(Collectors.toList());
                feedback.setResponse(responses);
            }

        return mapToDto(feedbackRepository.save(feedback));
    }


    public List<FeedbackDto> findAllFeedbacks() {
        return feedbackRepository.findAll()
                .stream().map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public FeedbackDto findFeedbackById(Long id) {
        Feedback feedback;
        try {
            feedback = feedbackRepository.findById(id)
                    .orElseThrow(() -> new NosuchExistException("feedback introuvable ou inexistant"));
        } catch (NosuchExistException e){
            System.err.println("Erreur lors de la recherche :" + e.getMessage());
            throw e;
        }
        return mapToDto(feedback);
    }

    @Transactional
    public boolean deleteFeedback(Long id) {
        log.info("deleting feedback : {}", id);
        Optional<Feedback> feedback = feedbackRepository.findById(id);
        if (feedback.isPresent()) {
            feedbackRepository.delete(feedback.get());
            log.info("feedback deleted");
            return true;
        }
            log.info("feedback not found");
            return false;
    }

    public List<FeedbackDto> feedbackByStaffId (Long staffId) {
        return feedbackRepository.findByStaffId(staffId)
                .stream().map(this::mapToDto)
                .collect(Collectors.toList());
    }

//    public double averageScore(Feedback feedback) {
//        if (feedback.getResponse() == null || feedback.getResponse().isEmpty()) {
//            return 0.0;
//        }
//
//        double total = feedback.getResponse()
//                .stream()
//                .mapToDouble(Response::getValue)
//                .sum();
//
//        return total / feedback.getResponse().size();
//    }

    public double averageScore(Long feedbackId) {
//        return Optional.ofNullable(feedbackRepository.findAverageScoreByFeedbackId(feedbackId))
//                .orElse(0.0);
        return feedbackRepository.findAverageScoreByFeedbackId(feedbackId);
    }
}
