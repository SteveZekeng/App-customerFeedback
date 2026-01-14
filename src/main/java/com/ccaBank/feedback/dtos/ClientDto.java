package com.ccaBank.feedback.dtos;

import jakarta.validation.constraints.Email;
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
    private String username;

    @Email
    private String email;

    @NotNull @Size(min = 6 , max = 15)
    private String password;

    @NotNull
    private String firstName;

    @NotNull @Size(min=9, max=9)
    private String phone;

    @NotNull
    private String ville;

    private Long numeroCompte;

}
