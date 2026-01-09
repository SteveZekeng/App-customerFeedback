package com.ccaBank.feedback.dtos;

import com.ccaBank.feedback.entities.Agence;
import com.ccaBank.feedback.entities.Client;
import com.ccaBank.feedback.entities.ServiceBancaire;
import com.ccaBank.feedback.entities.StatutRDV;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RDVDto {

    private Long id;

    @NotNull
    private String referenceNumber;

    private StatutRDV statutRDV;

    @NotNull
    private LocalDateTime date_heure;
    @NotNull
    private ClientDto client_id;

    @NotNull
    private ServiceBancaireDto serviceBancaire_id;

    @NotNull
    private AgenceDto agence_id;
}
