package com.ccaBank.feedback.services;

import com.ccaBank.feedback.dtos.*;
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

import java.util.ArrayList;
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
            StaffDto staffDto = modelMapper.map(feedback.getStaff(), StaffDto.class);
            feedbackDto.setStaff_id(staffDto);
        }

        if (feedback.getResponses() != null &&  !feedback.getResponses().isEmpty()) {
            List<ResponseDto> responseDto = feedback.getResponses()
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

            if (feedbackDto.getStaff_id() != null && feedbackDto.getStaff_id().getId() != null) {
                Staff staff = staffRepository.findById(feedbackDto.getStaff_id().getId())
                                .orElseThrow(() -> new NosuchExistException("agence introuvable"));
                feedback.setStaff(staff);
            }

        if (feedbackDto.getResponses() != null && !feedbackDto.getResponses().isEmpty()) {

            List<Response> responses = feedbackDto.getResponses()
                    .stream()
                    .map(responseDto -> {

                        if (responseDto.getQuestion_id() == null) {
                            throw new NosuchExistException("question_id manquant dans une réponse");
                        }

                        Question question = questionRepository.findById(responseDto.getQuestion_id())
                                .orElseThrow(() -> new NosuchExistException(
                                        "question introuvable avec id : " + responseDto.getQuestion_id()));

                        Response response = new Response();
                        response.setFeedback(feedback);
                        response.setQuestion(question);

                        if (responseDto.getSelectedLabel() != null &&
                                !responseDto.getSelectedLabel().isEmpty()) {

                            Optional<Proposition> propOpt = question.getProposition()
                                    .stream()
                                    .filter(p -> p.getLabel().equalsIgnoreCase(responseDto.getSelectedLabel()))
                                    .findFirst();

                            if (propOpt.isEmpty()) {
                                throw new NosuchExistException("proposition introuvable pour la question : "
                                        + question.getLabelQuestion());
                            }

                            Proposition prop = propOpt.get();
                            response.setSelectedLabel(prop.getLabel());
                            response.setValue(prop.getScore());
                        } else {
                            response.setSelectedLabel("");
                            response.setValue(responseDto.getValue());
                        }

                        return response;
                    })
                    .collect(Collectors.toList());

            feedback.setResponses(responses);
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

    public void deleteFeedback(Long id) {
        Optional<Feedback> feedback = feedbackRepository.findById(id);
        if (!feedback.isPresent()) {
            throw new NosuchExistException("feedback introuvable ou inexistant");
        }
        feedbackRepository.deleteById(id);
        log.info("feedback deleted successfully");
    }

    public List<FeedbackDto> feedbackByStaffId (Long staffId) {
        return feedbackRepository.findByStaffId(staffId)
                .stream().map(this::mapToDto)
                .collect(Collectors.toList());
    }


    public FeedbackDto getFeedbackFormByStaffMatricule(String matricule) {

        Staff staff = staffRepository.findByMatricule(matricule)
                .orElseThrow(() -> new NosuchExistException("Aucun staff trouvé avec le matricule : " + matricule));

        staff.setMatricule(matricule);

        List<QuestionDto> questionDto = questionRepository.findAllByOrderByIndexOrderAsc()
                .stream().map(q -> {
                    QuestionDto dto = new QuestionDto();
                    dto.setId(q.getId());
                    dto.setLabelQuestion(q.getLabelQuestion());
                    dto.setInputType(q.getInputType());
                    dto.setIndexOrder(q.getIndexOrder());
                    dto.setPropositions(
                            q.getProposition().stream()
                                    .map(p -> new PropositionDto(p.getLabel(), p.getScore()))
                                    .toList()
                    );

                    return dto;
                }).toList();

        List<ResponseDto> responseDto = questionRepository.findAll()
                .stream().map(q -> {
                    ResponseDto r = new ResponseDto();
                    r.setQuestion_id(q.getId());
                    r.setSelectedLabel("");
                    r.setValue(0);
                return r;
        }).toList();



        FeedbackDto feedbackDto = new FeedbackDto();
        feedbackDto.setCustomerName("");
        feedbackDto.setCustomerPhone("");
        feedbackDto.setComment("");
        feedbackDto.setStaff_id(modelMapper.map(staff, StaffDto.class));
        feedbackDto.setQuestions(questionDto);
        feedbackDto.setResponses(responseDto);


        return feedbackDto;
    }

    public Double averageScoreByStaff(Long staffId) {

        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new NosuchExistException("Le staff avec l'id " + staffId + " n'existe pas"));

        return staff.getAverage();
    }

    public Double averageScore(Long feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new NosuchExistException("Le feedback avec l'id " + feedbackId + " n'existe pas"));

        return feedback.getAverage();
    }



}
