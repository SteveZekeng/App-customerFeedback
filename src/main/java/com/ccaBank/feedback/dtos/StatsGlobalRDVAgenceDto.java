package com.ccaBank.feedback.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsGlobalRDVAgenceDto {

    private String agenceName;
    private Long totalRDV;
}
