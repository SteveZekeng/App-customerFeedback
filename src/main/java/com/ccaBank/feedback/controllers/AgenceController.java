package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.AgenceDto;
import com.ccaBank.feedback.dtos.RDVDto;
import com.ccaBank.feedback.services.AgenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customFeedback/agence")
@Tag(name = "Agences", description = "Agence REST API")

public class AgenceController {

    private final AgenceService  agenceService;

    public AgenceController(AgenceService agenceService) {
        this.agenceService = agenceService;
    }


    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = AgenceDto.class))}),
            @ApiResponse(responseCode = "404", description = "Agence not found",
                    content = @Content)})
    @Operation(summary = "Creation d'une agence")
    @PostMapping
    public AgenceDto addAgence(@RequestBody AgenceDto agenceDto) {
        return agenceService.createAgence(agenceDto);
    }

    @Operation(summary = "Liste de toutes les agences")
    @GetMapping
    public Page<AgenceDto> getAllAgences(Pageable pageable) {
        return agenceService.findAllAgences(pageable);
    }

    @GetMapping("/selectGFA")
    public List<AgenceDto> getAllAgence() {
        return agenceService.findAllAgence();
    }


    @Operation(summary = "Retourner le detail d'une agence")
    @GetMapping("/{id}")
    public AgenceDto getAgenceById(@PathVariable("id") Long id) {
        return agenceService.findAgenceById(id);
    }

    @Operation(summary = "Mise a jour d'une agence")
    @PutMapping("/{id}")
    public AgenceDto updateAgenceById(@PathVariable("id") Long id,
                                               @RequestBody AgenceDto agenceDto) {
        return agenceService.updateAgence(id, agenceDto);
    }

    @Operation(summary = "Suppression d'une agence")
    @DeleteMapping("/{id}")
    public void deleteAgenceById(@PathVariable("id") Long id) {
        agenceService.deleteAgence(id);
    }

    @Operation(summary = "Score d'un feedback par agence", description = "Retourne le score moyen " +
            "d'une agence en fonction des feedback recus par les staffs de cette agence.")
    @GetMapping("/avgAgence/{agenceId}")
    public Double getAvgScoreByAgence(@PathVariable("agenceId") Long agenceId) {
        return agenceService.averageScoreByAgence(agenceId);
    }

    @Operation(summary = "Liste des agences par scores decroissants", description = "liste les agences " +
            "ayant les scoring de feedback les plus élevés aux moins élevés.")
    @GetMapping("/listAgenceOrderDesc")
    public List<AgenceDto> getListAgenceOrder() {
        return agenceService.listAgenceOrder();
    }
}
