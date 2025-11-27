package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.LoginCreds;
import com.ccaBank.feedback.entities.User;
import com.ccaBank.feedback.repositories.UserRepository;
import com.ccaBank.feedback.util.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/customFeedback/auth")

public class AuthController {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, JWTUtil jwtUtil,
                          AuthenticationManager authenticationManager,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
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
    public Map<String, Object> loginHandler(@RequestBody LoginCreds body) {
        try {
            // Authentifier l’utilisateur
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword());
            authenticationManager.authenticate(authInputToken);

            // Génére le token JWT
            String token = jwtUtil.generateToken(body.getUsername());
            return Collections.singletonMap("jwt-token", token);
        } catch (AuthenticationException authExc) {
            throw new RuntimeException("Invalid username/password.");
        }
    }
}
