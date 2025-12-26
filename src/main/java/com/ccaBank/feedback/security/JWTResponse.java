package com.ccaBank.feedback.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWTResponse {

    private String token;
    private String type;
    private String refreshToken;
    private Long id;
    private String username;
    private String email;
}
