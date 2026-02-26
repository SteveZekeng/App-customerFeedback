package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.ResponseDto;
import com.ccaBank.feedback.dtos.ServiceBancaireDto;
import com.ccaBank.feedback.entities.Category;
import com.ccaBank.feedback.entities.ServiceBancaire;
import com.ccaBank.feedback.services.ServiceBancaireService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gestionFA/serviceBancaire")
@Tag(name = "BankService", description = "Bank_S REST API")

public class ServiceBancaireController {

    private final ServiceBancaireService serviceBancaireService;
    public ServiceBancaireController(ServiceBancaireService serviceB) {
        this.serviceBancaireService = serviceB;
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ServiceBancaire.class))}),
            @ApiResponse(responseCode = "404", description = "Service_B not found",
                    content = @Content)})
    @Operation(summary = "Ajout d'un service bancaire")
    @PostMapping
    public ServiceBancaireDto addService(@RequestBody ServiceBancaireDto serviceBancaireDto) {
        return serviceBancaireService.createServiceBancaire(serviceBancaireDto);
    }

    @Operation(summary = "Liste des services bancaires")
    @GetMapping
    public List<ServiceBancaireDto> getAllServiceBancaire() {
        return serviceBancaireService.getAllServiceBancaire();
    }

    @Operation(summary = "Retourner un service bancaire (detail)")
    @GetMapping("/{id}")
    public ServiceBancaireDto getServiceBancaire(@PathVariable Long id) {
        return serviceBancaireService.findServiceBancaireById(id);
    }

    @Operation(summary = "Mise a jour d'un service bancaire")
    @PutMapping("/{id}")
    public ServiceBancaireDto updateServiceB(@PathVariable Long id,
                                             @RequestBody ServiceBancaireDto serviceBancaireDto) {
        return serviceBancaireService.updateServiceBancaire(serviceBancaireDto, id);
    }

    @Operation(summary = "Suppression d'un service bancaire")
    @DeleteMapping("/{id}")
    public void deleteServiceBancaire(@PathVariable Long id) {
        serviceBancaireService.deleteServiceBancaire(id);
    }

    @Operation(summary = "Liste des services d'une categorie")
    @GetMapping("/category/{category}")
    public List<ServiceBancaireDto> getByCategory(@PathVariable Category category) {
        return serviceBancaireService.getServicesByCategory(category);
    }


}
