package com.ccaBank.feedback.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgenceDto {
    
    private Long id;

    @NotNull
    private String agenceMatriculate;

    @NotNull
    private String agenceCity;

    @NotNull
    private String agenceLocation;

    private double average;

}
