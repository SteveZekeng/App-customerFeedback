package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.FeedbackDto;
import com.ccaBank.feedback.services.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/customFeedback/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/addFeedback")
    public FeedbackDto addFeedback(@RequestBody FeedbackDto feedbackDto){

        return  feedbackService.createFeedback(feedbackDto);
    }

    @GetMapping
    public List<FeedbackDto> getAllFeedbacks(){

        return feedbackService.findAllFeedbacks();
    }

    @GetMapping("/{id}")
    public FeedbackDto getFeedbackById(@PathVariable("id") Long id){

        return feedbackService.findFeedbackById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteFeedbackById(@PathVariable("id") Long id){
        feedbackService.deleteFeedback(id);
    }

    @GetMapping("/form/{matricule}")
    public List<FeedbackDto> feedbackByMatricule(@PathVariable("matricule") String matricule){
        return Collections.singletonList(feedbackService.getFeedbackFormByStaffMatricule(matricule));
    }

    @GetMapping("/feedbackStaff/{staffId}")
    public List<FeedbackDto> getAllByStaffId(@PathVariable("staffId") Long staffId){
        return feedbackService.feedbackByStaffId(staffId);
    }

    @GetMapping("/avgStaff/{staffId}")
    public Double getAverageScoreStaff(@PathVariable Long staffId) {
        return feedbackService.averageScoreByStaff(staffId);
    }

    @GetMapping("/scoring/{feedbackId}")
    public double getScoring(@PathVariable("feedbackId") Long feedbackId){
        return feedbackService.averageScore(feedbackId);
    }

}
