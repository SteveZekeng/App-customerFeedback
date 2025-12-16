package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.QuestionDto;
import com.ccaBank.feedback.services.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customFeedback/question")

public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public QuestionDto addQuestion(@RequestBody QuestionDto questionDto){

        return  questionService.createQuestion(questionDto);
    }

    @GetMapping
    public List<QuestionDto> getAllQuestions(){

        return questionService.findAllQuestions();
    }

    @GetMapping("/{id}")
    public QuestionDto getQuestionById(@PathVariable("id") Long id){

        return questionService.findQuestionById(id);
    }

    @PutMapping("/{id}")
    public QuestionDto updateQuestionById(@PathVariable("id") Long id,
                                              @RequestBody QuestionDto questionDto){
        return questionService.updateQuestion(id, questionDto);
    }

    @DeleteMapping("/{id}")
    public void deleteQuestionById(@PathVariable("id") Long id){
       questionService.deleteQuestion(id);
    }

}
