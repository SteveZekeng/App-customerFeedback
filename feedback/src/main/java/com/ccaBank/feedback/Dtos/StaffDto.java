package com.ccaBank.feedback.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffDto {

    @NotNull
    private String staffName;

    @NotNull @Min(9) @Max(9)
    private String staffPhone;

    @NotNull @Email
    private String staffEmail;
}
