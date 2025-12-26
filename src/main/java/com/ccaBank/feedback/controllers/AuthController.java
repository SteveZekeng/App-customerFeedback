package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.LoginCreds;
import com.ccaBank.feedback.dtos.TokenRefreshRequest;
import com.ccaBank.feedback.entities.RefreshToken;
import com.ccaBank.feedback.entities.User;
import com.ccaBank.feedback.repositories.UserRepository;
import com.ccaBank.feedback.security.JWTResponse;
import com.ccaBank.feedback.security.TokenRefreshResponse;
import com.ccaBank.feedback.services.RefreshTokenService;
import com.ccaBank.feedback.util.JWTUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/customFeedback/auth")

public class AuthController {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public AuthController(UserRepository userRepository, JWTUtil jwtUtil,
                          AuthenticationManager authenticationManager,
                          PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/register")
    public Map<String, Object> registerHandler(@RequestBody User user) {
        // Encoder le mot de passe avant sauvegarde
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);

        // Génére un JWT pour l’utilisateur
        String token = jwtUtil.generateToken(user.getUsername());
        return Collections.singletonMap("jwt-token", token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginHandler(@RequestBody LoginCreds body) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        body.getUsername(),
                        body.getPassword()
                )
        );

        User user = userRepository.findByUsername(body.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String jwt = jwtUtil.generateToken(user.getUsername());

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        return ResponseEntity.ok(
                new JWTResponse(
                        jwt,
                        "Bearer",
                        refreshToken.getToken(),
                        user.getId(),
                        user.getUsername(),
                        user.getEmail()
                )
        );
    }


    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(
            @Valid @RequestBody TokenRefreshRequest request) {

        RefreshToken refreshToken =
                refreshTokenService.verifyExpiration(
                        refreshTokenService.getByToken(request.getRefreshToken())
                );

        String newAccessToken =
                jwtUtil.generateToken(refreshToken.getUser().getUsername());

        return ResponseEntity.ok(
                new TokenRefreshResponse(
                        newAccessToken,
                        refreshToken.getToken(),
                        "Bearer"
                )
        );
    }


}
