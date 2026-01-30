package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.RDVDto;
import com.ccaBank.feedback.dtos.StaffDto;
import com.ccaBank.feedback.services.RDVService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gestionFA/RDV")
public class RDVController {

    private final RDVService rdvService;
    public RDVController(RDVService rdvService) {
        this.rdvService = rdvService;
    }

    @PostMapping
    public RDVDto createRDV(@RequestBody RDVDto dto, Authentication auth) {
        return rdvService.createRDV(dto, auth.getName());
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping
    public List<RDVDto> getAllRDVs() {
        return rdvService.getAllRDVs();
    }

    @GetMapping("/{id}")
    public RDVDto getRDV(@PathVariable Long id,
            Authentication authentication) {

        return rdvService.getRDVForClient(id, authentication.getName());
    }


    @PutMapping("/{id}")
    public RDVDto updateRDV(@RequestBody RDVDto rdvDto, @PathVariable Long id) {
        return rdvService.updateRDV(rdvDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteRDV(@PathVariable Long id) {
        rdvService.deleteRDV(id);
    }

    @GetMapping("/clientRDV")
    @PreAuthorize("hasRole('CLIENT')")
    public List<RDVDto> getMyRDVs(Authentication authentication) {
        return rdvService.findRDVsByClientUsername(authentication.getName());
    }


    @PostMapping("/{id}/notify-admins")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<?> notifyAdmins(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        rdvService.notifyAdminsOfAgence(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/allRDV")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public List<RDVDto> dashboardRDVs(Authentication auth) {
        return rdvService.getRDVsForDashboard(auth.getName());
    }

    @PutMapping("/{id}/confirmer")
    @PreAuthorize("hasRole('STAFF')")
    public RDVDto confirmerRDV(@PathVariable Long id, Authentication auth) {
        return rdvService.confirmerRDV(id, auth.getName());
    }

    @PutMapping("/{id}/honorer")
    public void honorRDV(@PathVariable Long id) {
         this.rdvService.honorerRDV(id);
    }
}
