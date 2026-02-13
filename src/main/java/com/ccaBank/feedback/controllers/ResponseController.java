package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.LoginCreds;
import com.ccaBank.feedback.dtos.ResponseDto;
import com.ccaBank.feedback.services.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customFeedback/response")
@Tag(name = "Responses", description = "Responses REST API")

public class ResponseController {

    private final ResponseService responseService;

    public ResponseController(ResponseService responseService) {
        this.responseService = responseService;
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Response not found",
                    content = @Content)})
    @Operation(summary = "Enregistrement d'une reponse")
    @PostMapping
    public ResponseEntity<String> addResponse(@RequestBody ResponseDto responseDto){
        responseService.createResponse(responseDto);
        return  new ResponseEntity<>("Response saved successfully", HttpStatus.CREATED);
    }

    @Operation(summary = "Liste des reponses")
    @GetMapping
    public List<ResponseDto> getAllReponses(){

        return responseService.findAllResponses();
    }

    @GetMapping("/{id}")
    public ResponseDto getResponseById(@PathVariable("id") Long id){

        return responseService.findResponseById(id);
    }

    @Operation(summary = "Suppression d'une reponse")
    @DeleteMapping("/{id}")
    public void deleteResponseById(@PathVariable("id") Long id){
        responseService.deleteResponse(id);
    }

    @Operation(summary = "Liste des responses par feedback")
    @GetMapping("/responseFeedback/{feedbackId}")
    public List<ResponseDto> getAllByFeedbackId(@PathVariable("feedbackId") Long feedbackId){
        return responseService.responseByFeedbackId(feedbackId);
    }


}
