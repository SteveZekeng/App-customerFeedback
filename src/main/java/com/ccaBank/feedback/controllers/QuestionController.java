package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.LoginCreds;
import com.ccaBank.feedback.dtos.QuestionDto;
import com.ccaBank.feedback.services.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customFeedback/question")
@Tag(name = "Questions", description = "Questions REST API")

public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = QuestionDto.class))}),
            @ApiResponse(responseCode = "404", description = "Question not found",
                    content = @Content)})
    @Operation(summary = "Ajout d'une question", description = "Au moment de la creation d'une question; " +
            "l'utilisateur crée directement ses propositions de reponses (si existe)")
    @PostMapping
    public QuestionDto addQuestion(@RequestBody QuestionDto questionDto){

        return  questionService.createQuestion(questionDto);
    }

    @Operation(summary = "Liste des questions")
    @GetMapping
    public List<QuestionDto> getAllQuestions(){

        return questionService.findAllQuestions();
    }

    @Operation(summary = "Retourne une question")
    @GetMapping("/{id}")
    public QuestionDto getQuestionById(@PathVariable("id") Long id){

        return questionService.findQuestionById(id);
    }

    @Operation(summary = "Mise a jour d'une question")
    @PutMapping("/{id}")
    public QuestionDto updateQuestionById(@PathVariable("id") Long id,
                                              @RequestBody QuestionDto questionDto){
        return questionService.updateQuestion(id, questionDto);
    }

    @Operation(summary = "Suppression d'une question")
    @DeleteMapping("/{id}")
    public void deleteQuestionById(@PathVariable("id") Long id){
       questionService.deleteQuestion(id);
    }

}
