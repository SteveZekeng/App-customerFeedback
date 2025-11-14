package com.ccaBank.feedback.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgenceDto {

    @NotNull
    private String agenceCity;

    @NotNull
    private String agenceLocation;

    private double average;
}
