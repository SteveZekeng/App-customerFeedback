package com.ccaBank.feedback.dtos;

import com.ccaBank.feedback.entities.StatutRDV;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @NotNull @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateHeure;

    private ClientDto client_Id;

    @NotNull
    private AgenceDto agence_Id;

    @NotNull
    private ServiceBancaireDto serviceBancaire_Id;

    private String staffMatricule;

}
