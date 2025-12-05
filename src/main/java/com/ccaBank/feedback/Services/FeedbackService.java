package com.ccaBank.feedback.services;

import com.ccaBank.feedback.dtos.FeedbackDto;
import com.ccaBank.feedback.dtos.PropositionDto;
import com.ccaBank.feedback.dtos.QuestionDto;
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
            Staff staffDto = feedback.getStaff();
            feedbackDto.setStaff_id(staffDto.getId());
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

        ResponseDto responseDto = new ResponseDto();
        responseDto.setValue(5.0);
        responseDto.setSelectedLabel("Excellence");
        List<ResponseDto> responsesDto = new ArrayList<>();
        responsesDto.add(responseDto);

        Response response = new Response();
        response.setValue(5.0);
        response.setSelectedLabel("Excellence");
        List<Response> responses = new ArrayList<>();
        responses.add(response);

        Feedback feedback = new Feedback();
        feedback.setCustomerName("Phil");
        feedback.setCustomerPhone("655479987");
        feedback.setComment("RAS");
        feedback.setStaff(staff);
        feedback.setResponses(responses);

        List<QuestionDto> questionDto = questionRepository.findAll()
                .stream()
                .map(q -> {
                    QuestionDto qDto = new QuestionDto();
                    qDto.setLabelQuestion(q.getLabelQuestion());
                    qDto.setPropositions(
                            q.getProposition().stream()
                                    .map(p -> new PropositionDto(p.getLabel(), p.getScore()))
                                    .collect(Collectors.toList())
                    );
                    return qDto;
                })
                .toList();

        FeedbackDto feedbackDto = new FeedbackDto();
        feedbackDto.setCustomerName(feedback.getCustomerName());
        feedbackDto.setCustomerPhone(feedback.getCustomerPhone());
        feedbackDto.setQuestions(questionDto);
        feedbackDto.setComment(feedback.getComment());
        feedbackDto.setStaff_id(staff.getId());
        feedbackDto.setResponses(responsesDto.stream().map(r1 -> {
            ResponseDto responseDto2 = new ResponseDto();
            responseDto2.setValue(r1.getValue());
            responseDto2.setSelectedLabel(r1.getSelectedLabel());
            responseDto2.setFeedback_id(r1.getFeedback_id());
            responseDto2.setQuestion_id(r1.getQuestion_id());
            return responseDto2;
        }).collect(Collectors.toList()));


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
