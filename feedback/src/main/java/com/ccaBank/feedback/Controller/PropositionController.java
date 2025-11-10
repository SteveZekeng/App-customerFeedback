package com.ccaBank.feedback.Controller;

import com.ccaBank.feedback.Dtos.PropositionDto;
import com.ccaBank.feedback.Entities.Proposition;
import com.ccaBank.feedback.Services.PropositionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/custumFeedback/proposition")
public class PropositionController {

    private final PropositionService propositionService;

    public PropositionController(PropositionService propositionService){
        this.propositionService = propositionService;
    }

    @PostMapping
    public ResponseEntity<String> addProposition(@RequestBody PropositionDto propositionDto){
        propositionService.createProposition(propositionDto);
        return  new ResponseEntity<>("proposition created successfully", HttpStatus.CREATED);
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
    public ResponseEntity<String> deleteProposotionById(@PathVariable Long id){
        propositionService.deleteProposition(id);
        return ResponseEntity.ok("proposition deleted successfully");
    }
}
