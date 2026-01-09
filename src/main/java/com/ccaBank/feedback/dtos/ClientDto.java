package com.ccaBank.feedback.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {

    private Long id;

    @NotNull
    private String firstName;

    @NotNull @Size(min=9, max=9)
    private String phone;

}
