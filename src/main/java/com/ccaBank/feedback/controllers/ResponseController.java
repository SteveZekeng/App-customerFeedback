package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.ResponseDto;
import com.ccaBank.feedback.services.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customFeedback/response")
public class ResponseController {

    private final ResponseService responseService;

    public ResponseController(ResponseService responseService) {
        this.responseService = responseService;
    }

    @PostMapping
    public ResponseEntity<String> addResponse(@RequestBody ResponseDto responseDto){
        responseService.createResponse(responseDto);
        return  new ResponseEntity<>("Response saved successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public List<ResponseDto> getAllReponses(){

        return responseService.findAllResponses();
    }

    @GetMapping("/{id}")
    public ResponseDto getResponseById(@PathVariable("id") Long id){

        return responseService.findResponseById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteResponseById(@PathVariable("id") Long id){
        boolean deleted = responseService.deleteResponse(id);
        if (deleted)
            return ResponseEntity.ok("Deleted successfully");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
    }

    @GetMapping("/responseFeedback/{feedbackId}")
    public List<ResponseDto> getAllByFeedbackId(@PathVariable("feedbackId") Long feedbackId){
        return responseService.findByFeedbackId(feedbackId);
    }


}
