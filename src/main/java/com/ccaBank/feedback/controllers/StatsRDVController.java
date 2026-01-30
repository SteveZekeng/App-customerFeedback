package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.StatsGlobalRDVAgenceDto;
import com.ccaBank.feedback.dtos.StatsRDVAgenceDto;
import com.ccaBank.feedback.services.StatsRDVService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gestionFA/statistiques")
public class StatsRDVController {

    private final StatsRDVService statsRDVService;

    public StatsRDVController(StatsRDVService statsRDVService) {
        this.statsRDVService = statsRDVService;
    }

    @GetMapping("/statAgence")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    public StatsRDVAgenceDto statsAgence(Authentication auth) {
        return statsRDVService.getStatsForAgenceOfUser(auth.getName());
    }

    @GetMapping("/allAgences")
    @PreAuthorize("hasRole('ADMIN')")
    public List<StatsGlobalRDVAgenceDto> statsAllAgences() {
        return statsRDVService.getGlobalStats();
    }

}
