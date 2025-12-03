package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.FeedbackDto;
import com.ccaBank.feedback.services.FeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/customFeedback/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/addFeedback")
    public ResponseEntity<String> addFeedback(@RequestBody FeedbackDto feedbackDto){
        feedbackService.createFeedback(feedbackDto);
        return  new ResponseEntity<>("feedback created successfully", HttpStatus.CREATED);
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

    @GetMapping("/feedbackStaff/{staffId}")
    public List<FeedbackDto> getAllByStaffId(@PathVariable("staffId") Long staffId){
        return feedbackService.feedbackByStaffId(staffId);
    }

    @GetMapping("/scoring/{feedbackId}")
    public double getScoring(@PathVariable("feedbackId") Long feedbackId){
        return feedbackService.averageScore(feedbackId);
    }

    @GetMapping("/form/{matricule}")
    public List<FeedbackDto> feedbackByMatricule(@PathVariable("matricule") String matricule){
        return Collections.singletonList(feedbackService.getFeedbackFormByStaffMatricule(matricule));
    }

    @GetMapping("/avgStaff/{staffId}")
    public Double getAverageScoreStaff(@PathVariable Long staffId) {
        return feedbackService.averageScoreByStaff(staffId);
    }


}
