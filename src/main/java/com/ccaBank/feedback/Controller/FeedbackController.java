package com.ccaBank.feedback.Controller;

import com.ccaBank.feedback.Dtos.FeedbackDto;
import com.ccaBank.feedback.Dtos.ResponseDto;
import com.ccaBank.feedback.Services.FeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customFeedback/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
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
    public ResponseEntity<String> deleteFeedbackById(@PathVariable Long id){
        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok("Feedback deleted successfully");
    }

    @GetMapping("/{feedbackId}")
    public List<ResponseDto> getByFeedbackId(@PathVariable("feedbackId") Long feedbackId){
        return feedbackService.ResponsesByFeedbackId(feedbackId);
    }

}
