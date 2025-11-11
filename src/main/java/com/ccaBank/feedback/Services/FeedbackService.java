package com.ccaBank.feedback.services;

import com.ccaBank.feedback.dtos.FeedbackDto;
import com.ccaBank.feedback.dtos.ResponseDto;
import com.ccaBank.feedback.entities.Feedback;
import com.ccaBank.feedback.entities.Response;
import com.ccaBank.feedback.entities.Staff;
import com.ccaBank.feedback.exceptions.NosuchExistException;
import com.ccaBank.feedback.repositories.FeedbackRepository;
import com.ccaBank.feedback.repositories.StaffRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final StaffRepository staffRepository;
    private final ModelMapper modelMapper;

    public FeedbackService(FeedbackRepository feedbackRepository,
                           StaffRepository staffRepository,
                           ModelMapper modelMapper) {
        this.feedbackRepository = feedbackRepository;
        this.staffRepository = staffRepository;
        this.modelMapper = modelMapper;
    }

    private FeedbackDto mapToDto(Feedback feedback) {
        FeedbackDto feedbackDto = modelMapper.map(feedback, FeedbackDto.class);
        return feedbackDto;
    }

    private Feedback mapToEntity(FeedbackDto feedbackDto) {
        return modelMapper.map(feedbackDto, Feedback.class);
    }

    public FeedbackDto createFeedback(FeedbackDto feedbackDto) {
        Feedback feedback = mapToEntity(feedbackDto);
        try {
            if (feedbackDto.getStaffId() != null) {
                Staff staff = staffRepository.findById(feedbackDto.getStaffId()).orElseThrow(() ->
                        new NosuchExistException("staff introuvable"));
                feedback.setStaff(staff);
            }
        }catch (NosuchExistException ex) {
            System.err.println("Erreur lors de la creation feedback :" + ex.getMessage());
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

    public boolean deleteFeedback(Long id) {
        if (feedbackRepository.existsById(id)) {
            feedbackRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<ResponseDto> ResponsesByFeedbackId(Long feedbackId) {
        return feedbackRepository.ResponsesByFeedbackId(feedbackId)
                .stream().map(r -> modelMapper.map(r, ResponseDto.class))
                .collect(Collectors.toList());
    }

    public double averageScore(Feedback feedback) {
        if (feedback.getResponse() == null || feedback.getResponse().isEmpty()) {
            return 0.0;
        }

        double total = feedback.getResponse()
                .stream()
                .mapToDouble(Response::getValue)
                .sum();

        return total / feedback.getResponse().size();
    }
}
