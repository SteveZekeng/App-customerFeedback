package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.FeedbackDto;
import com.ccaBank.feedback.dtos.LoginCreds;
import com.ccaBank.feedback.services.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/customFeedback/feedback")
@Tag(name = "Feedbacks", description = "Feedback REST API")

public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = FeedbackDto.class))}),
            @ApiResponse(responseCode = "404", description = "Feedback not found",
                    content = @Content)})
    @Operation(summary = "Enregistrement d'un feedback")
    @PostMapping("/addFeedback")
    public FeedbackDto addFeedback(@RequestBody FeedbackDto feedbackDto){

        return  feedbackService.createFeedback(feedbackDto);
    }

    @Operation(summary = "Liste des feedbacks")
    @GetMapping
    public Page<FeedbackDto> getAllFeedbacks(Pageable pageable) {

        return feedbackService.findAllFeedbacks(pageable);
    }

    @Operation(summary = "Retourner le detail d'un feedback")
    @GetMapping("/{id}")
    public FeedbackDto getFeedbackById(@PathVariable("id") Long id){

        return feedbackService.findFeedbackById(id);
    }

    @Operation(summary = "Suppression d'un feedback")
    @DeleteMapping("/{id}")
    public void deleteFeedbackById(@PathVariable("id") Long id){
        feedbackService.deleteFeedback(id);
    }

    @Operation(summary = "Formulaire de feedback client", description = "Retourne dynamiquement le " +
            "formulaire de feedback (questions, propositions... integrées.")
    @GetMapping("/form/{matricule}")
    public List<FeedbackDto> feedbackByMatricule(@PathVariable("matricule") String matricule){
        return Collections.singletonList(feedbackService.getFeedbackFormByStaffMatricule(matricule));
    }

    @Operation(summary = "Liste des feedbacks d'un staff")
    @GetMapping("/feedbackStaff/{staffId}")
    public List<FeedbackDto> getAllByStaffId(@PathVariable("staffId") Long staffId){
        return feedbackService.feedbackByStaffId(staffId);
    }

    @Operation(summary = "Score moyen des feedbacks d'un staff")
    @GetMapping("/avgStaff/{staffId}")
    public Double getAverageScoreStaff(@PathVariable Long staffId) {
        return feedbackService.averageScoreByStaff(staffId);
    }

    @Operation(summary = "Calcul du scoring d'un feedback ")
    @GetMapping("/scoring/{feedbackId}")
    public double getScoring(@PathVariable("feedbackId") Long feedbackId){
        return feedbackService.averageScore(feedbackId);
    }

}
