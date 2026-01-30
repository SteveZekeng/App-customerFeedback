package com.ccaBank.feedback.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsRDVAgenceDto {

    private String agenceName;
    private Long totalRDV;
    private Long rdvEnAttente;
    private Long rdvConfirmed;
    private Long rdvHonored;
}
