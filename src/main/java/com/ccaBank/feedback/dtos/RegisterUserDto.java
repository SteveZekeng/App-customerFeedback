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
public class RegisterUserDto {

    @NotNull
    private String username;

    @NotNull
    private String password;

    // Spécifique au client
    private String firstName;
    @Size(min=9, max=9)
    private String phone;
    private String ville;
    private String numeroCompte;
    @Email
    private String email;

    // Spécifique au staff
    private String staffName;
    @Size(min=9, max=9)
    private String staffPhone;
    @Email
    private String staffEmail;
    private String matricule;
    private AgenceDto agenceId;

    @NotNull
    private String typeUser;
}
