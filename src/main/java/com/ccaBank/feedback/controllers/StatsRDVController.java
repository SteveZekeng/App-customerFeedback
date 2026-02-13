package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.StaffDto;
import com.ccaBank.feedback.dtos.StatsGlobalRDVAgenceDto;
import com.ccaBank.feedback.dtos.StatsRDVAgenceDto;
import com.ccaBank.feedback.services.StatsRDVService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gestionFA/statistiques")
@Tag(name = "RDV_Statistiques", description = "StatsRDV REST API")

public class StatsRDVController {

    private final StatsRDVService statsRDVService;

    public StatsRDVController(StatsRDVService statsRDVService) {
        this.statsRDVService = statsRDVService;
    }

    @Operation(summary = "Retourne les statistiques d'une agence", description = "Elle fait une " +
            "analyse sur les RDV et ressort leur statistiques pour un visuel plus explicite en dashboard")
    @GetMapping("/statAgence")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    public StatsRDVAgenceDto statsAgence(Authentication auth) {
        return statsRDVService.getStatsForAgenceOfUser(auth.getName());
    }

    @Operation(summary = "Retourne les statistiques de toutes les agences a l'Admin uniquement")
    @GetMapping("/allAgences")
    @PreAuthorize("hasRole('ADMIN')")
    public List<StatsGlobalRDVAgenceDto> statsAllAgences() {
        return statsRDVService.getGlobalStats();
    }

}
