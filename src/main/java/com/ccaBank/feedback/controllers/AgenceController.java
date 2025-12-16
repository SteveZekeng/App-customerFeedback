package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.AgenceDto;
import com.ccaBank.feedback.services.AgenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customFeedback/agence")

public class AgenceController {

    private final AgenceService  agenceService;

    public AgenceController(AgenceService agenceService) {
        this.agenceService = agenceService;
    }

    @PostMapping
    public AgenceDto addAgence(@RequestBody AgenceDto agenceDto) {
        return agenceService.createAgence(agenceDto);
    }

    @GetMapping
    public List<AgenceDto> getAllAgences() {
        return agenceService.findAllAgences();
    }

    @GetMapping("/{id}")
    public AgenceDto getAgenceById(@PathVariable("id") Long id) {
        return agenceService.findAgenceById(id);
    }

    @PutMapping("/{id}")
    public AgenceDto updateAgenceById(@PathVariable("id") Long id,
                                               @RequestBody AgenceDto agenceDto) {
        return agenceService.updateAgence(id, agenceDto);
    }

    @DeleteMapping("/{id}")
    public void deleteAgenceById(@PathVariable("id") Long id) {
        agenceService.deleteAgence(id);
    }

    @GetMapping("/avgAgence/{agenceId}")
    public Double getAvgScoreByAgence(@PathVariable("agenceId") Long agenceId) {
        return agenceService.averageScoreByAgence(agenceId);
    }

    @GetMapping("/listAgenceOrderDesc")
    public List<AgenceDto> getListAgenceOrder() {
        return agenceService.listAgenceOrder();
    }
}
