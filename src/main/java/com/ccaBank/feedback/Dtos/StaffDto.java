package com.ccaBank.feedback.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffDto {

    private Long id;

    @NotNull
    private String staffName;

    @NotNull @Size(min=9, max=9)
    private String staffPhone;

    @NotNull @Email
    private String staffEmail;

    @NotNull
    private String matricule;

    @NotNull
    private AgenceDto agence_id;

    private double average;

}
