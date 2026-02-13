package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.AgenceDto;
import com.ccaBank.feedback.dtos.LoginCreds;
import com.ccaBank.feedback.dtos.RegisterUserDto;
import com.ccaBank.feedback.entities.*;
import com.ccaBank.feedback.security.JWTResponse;
import com.ccaBank.feedback.services.AuthService;
import com.ccaBank.feedback.services.OtpService;
import com.ccaBank.feedback.services.RefreshTokenService;
import com.ccaBank.feedback.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/customFeedback/auth")
@Tag(name = "Authentication", description = "Auth REST API")

public class AuthController {

    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final OtpService otpService;
    private final AuthService authService;

    public AuthController(JWTUtil jwtUtil,
                          AuthenticationManager authenticationManager,
                          RefreshTokenService refreshTokenService,
                          OtpService otpService, AuthService authService) {

        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.otpService = otpService;
        this.authService = authService;
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = RegisterUserDto.class))}),
            @ApiResponse(responseCode = "404", description = "Register failed",
                    content = @Content)})
    @Operation(summary = "Auth/register")
    @PostMapping("/register")
    public ResponseEntity<JWTResponse> register(@RequestBody RegisterUserDto dto) {
        JWTResponse response = authService.register(dto);
        return ResponseEntity.ok(response);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoginCreds.class))}),
            @ApiResponse(responseCode = "404", description = "Login failed",
                    content = @Content)})
    @Operation(summary = "Creation d'une agence")
    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(@RequestBody LoginCreds body) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword())
        );

        User user = authService.getUserByUsername(body.getUsername());
        String jwt = authService.generateToken(user);
        RefreshToken refreshToken = authService.createRefreshToken(user);

        return ResponseEntity.ok(new JWTResponse(
                jwt, "Bearer", refreshToken.getToken(),
                user.getId(), user.getUsername(), user.getRole()
        ));
    }


//    @PostMapping("/refreshtoken")
//    public ResponseEntity<?> refreshToken(
//            @Valid @RequestBody TokenRefreshRequest request) {
//
//        RefreshToken refreshToken =
//                refreshTokenService.verifyExpiration(
//                        refreshTokenService.getByToken(request.getRefreshToken())
//                );
//
//        String newAccessToken =
//                jwtUtil.generateToken(refreshToken.getUser().getUsername());
//
//        return ResponseEntity.ok(
//                new TokenRefreshResponse(
//                        newAccessToken,
//                        refreshToken.getToken(),
//                        "Bearer"
//                )
//        );
//    }


    @Operation(summary = "Genere l'OTP", description = "l'OTP est crée et envoyé automatiquement au client " +
            "pour qu'il confirme le reset de son password, elle sert de clé pour restaurer un password.")
    @PostMapping("/generate-otp")
    public ResponseEntity<Map<String, String>> generateOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        otpService.generateOtp(email);
        return ResponseEntity.ok(Collections.singletonMap("message", "L'OTP vous a été envoyé dans votre mail"));
    }


    @Operation(summary = "Valide l'OTP", description = "L'OTP entrée sert de clé pour valider " +
            "la création d'un nouveau mot de passe.")
    @PostMapping("/validate-otp")
    public ResponseEntity<Boolean> validateOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        boolean isValid = otpService.validateOtp(email, otp);
        return ResponseEntity.ok(isValid);
    }


}
