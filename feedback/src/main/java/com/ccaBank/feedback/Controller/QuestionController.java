package com.ccaBank.feedback.Controller;

import com.ccaBank.feedback.Dtos.QuestionDto;
import com.ccaBank.feedback.Services.QuestionService;
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
    public ResponseEntity<String> addQuestion(@RequestBody QuestionDto questionDto){
        questionService.createQuestion(questionDto);
        return  new ResponseEntity<>("question created successfully", HttpStatus.CREATED);
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
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        questionService.deleteQuestion(id);
        return ResponseEntity.ok("question deleted successfully");
    }

}
