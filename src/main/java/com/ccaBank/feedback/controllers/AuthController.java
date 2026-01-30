package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.LoginCreds;
import com.ccaBank.feedback.dtos.RegisterUserDto;
import com.ccaBank.feedback.dtos.TokenRefreshRequest;
import com.ccaBank.feedback.entities.*;
import com.ccaBank.feedback.repositories.AgenceRepository;
import com.ccaBank.feedback.repositories.ClientRepository;
import com.ccaBank.feedback.repositories.StaffRepository;
import com.ccaBank.feedback.repositories.UserRepository;
import com.ccaBank.feedback.security.JWTResponse;
import com.ccaBank.feedback.security.TokenRefreshResponse;
import com.ccaBank.feedback.services.AuthService;
import com.ccaBank.feedback.services.ClientService;
import com.ccaBank.feedback.services.OtpService;
import com.ccaBank.feedback.services.RefreshTokenService;
import com.ccaBank.feedback.util.JWTUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/customFeedback/auth")

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

    @PostMapping("/register")
    public ResponseEntity<JWTResponse> register(@RequestBody RegisterUserDto dto) {
        JWTResponse response = authService.register(dto);
        return ResponseEntity.ok(response);
    }

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

    @PostMapping("/generate-otp")
    public ResponseEntity<Map<String, String>> generateOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        otpService.generateOtp(email);
        return ResponseEntity.ok(Collections.singletonMap("message", "L'OTP vous a été envoyé dans votre mail"));
    }


    @PostMapping("/validate-otp")
    public ResponseEntity<Boolean> validateOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        boolean isValid = otpService.validateOtp(email, otp);
        return ResponseEntity.ok(isValid);
    }


}
