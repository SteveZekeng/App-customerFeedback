package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.PropositionDto;
import com.ccaBank.feedback.services.PropositionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customFeedback/proposition")
public class PropositionController {

    private final PropositionService propositionService;

    public PropositionController(PropositionService propositionService){
        this.propositionService = propositionService;
    }


    @PutMapping("/{id}")
    public PropositionDto updatePropositionById(@PathVariable("id") Long id,
                                              @RequestBody PropositionDto propositionDto){
        return propositionService.updateProposition(id, propositionDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePropositionById(@PathVariable Long id){
        propositionService.deleteProposition(id);
        return ResponseEntity.ok("proposition deleted successfully");
    }
}
