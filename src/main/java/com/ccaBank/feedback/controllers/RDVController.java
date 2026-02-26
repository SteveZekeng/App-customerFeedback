package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.RDVDto;
import com.ccaBank.feedback.services.RDVService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gestionFA/RDV")
@Tag(name = "Rendez-vous", description = "RDV REST API")

public class RDVController {

    private final RDVService rdvService;
    public RDVController(RDVService rdvService) {
        this.rdvService = rdvService;
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = RDVDto.class))}),
            @ApiResponse(responseCode = "404", description = "RDV not found",
                    content = @Content)})
    @Operation(summary = "Enregistrement d'un RDV")
    @PostMapping
    public RDVDto createRDV(@RequestBody RDVDto dto, Authentication auth) {
        return rdvService.createRDV(dto, auth.getName());
    }

    @Operation(summary = "Liste de tous les RDV")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping
    public List<RDVDto> getAllRDVs() {
        return rdvService.getAllRDVs();
    }

    @Operation(summary = "Retourner un RDV (detail)")
    @GetMapping("/{id}")
    public RDVDto getRDV(@PathVariable Long id,
            Authentication authentication) {

        return rdvService.getRDVForClient(id, authentication.getName());
    }


//    @PutMapping("/{id}")
//    public RDVDto updateRDV(@RequestBody RDVDto rdvDto, @PathVariable Long id) {
//        return rdvService.updateRDV(rdvDto, id);
//    }

    @Operation(summary = "Annuler un RDV")
    @DeleteMapping("/{id}")
    public void deleteRDV(@PathVariable Long id) {
        rdvService.deleteRDV(id);
    }

    @Operation(summary = "Liste des RDV d'un client")
    @GetMapping("/clientRDV")
    @PreAuthorize("hasRole('CLIENT')")
    public List<RDVDto> getMyRDVs(Authentication authentication) {
        return rdvService.findRDVsByClientUsername(authentication.getName());
    }


    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDetails.class))}),
            @ApiResponse(responseCode = "404", description = "Mail not found",
                    content = @Content)})
    @Operation(summary = "Mail de prise de RDV", description = "C'est un mail qui est envoyé automatiquement" +
            "du client vers l'agence sollicitée qui sera recu par tous les staffs de cette agence.")
    @PostMapping("/{id}/notify-admins")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<?> notifyAdmins(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        rdvService.notifyAdminsOfAgence(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Liste de RDV dashboard", description = "Cette liste ne se presente qu'en fonction" +
            "de l'agence du staff connecté, c'est a dire que chaque agence a ses RDV respectifs.")
    @GetMapping("/allRDV")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public List<RDVDto> dashboardRDVs(Authentication auth) {
        return rdvService.getRDVsForDashboard(auth.getName());
    }

    @Operation(summary = "Confirmation du RDV", description = "Ici, le staff doit valider/confirmer " +
            "le rdv du client. Parmi les staff de l'agence sollicité, un staff se charge de confirmer.")
    @PutMapping("/{id}/confirmer")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    public RDVDto confirmerRDV(@PathVariable Long id, Authentication auth) {
        return rdvService.confirmerRDV(id, auth.getName());
    }

    @Operation(summary = "Honorer un RDV", description = "A la fin du rdv, c'est a dire apres eu le " +
            "service dans l'agence sollicitée le client se doit de revenir dans son espace RDV pour " +
            "confirmé qu'il a bien honoré le rdv.")
    @PutMapping("/{id}/honorer")
    @PreAuthorize("hasRole('CLIENT')")
    public void honorRDV(@PathVariable Long id) {
         this.rdvService.honorerRDV(id);
    }
}
