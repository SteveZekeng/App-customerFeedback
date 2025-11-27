package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.PropositionDto;
import com.ccaBank.feedback.services.PropositionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/customFeedback/proposition")

public class PropositionController {

    private final PropositionService propositionService;

    public PropositionController(PropositionService propositionService){
        this.propositionService = propositionService;
    }

    @GetMapping
    public List<PropositionDto> getAllPropositions(){

        return propositionService.findAllPropositions();
    }

    @GetMapping("/{id}")
    public PropositionDto getPropositionById(@PathVariable("id") Long id){

        return propositionService.findPropositionById(id);
    }

    @PutMapping("/{id}")
    public PropositionDto updatePropositionById(@PathVariable("id") Long id,
                                              @RequestBody PropositionDto propositionDto){
        return propositionService.updateProposition(id, propositionDto);
    }

    @DeleteMapping("/{id}")
    public void deletePropositionById(@PathVariable("id") Long id){
       propositionService.deleteProposition(id);
    }
}
