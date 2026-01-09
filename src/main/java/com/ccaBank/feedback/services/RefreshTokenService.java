package com.ccaBank.feedback.services;

import com.ccaBank.feedback.entities.RefreshToken;
import com.ccaBank.feedback.entities.User;
import com.ccaBank.feedback.exceptions.TokenRefreshException;
import com.ccaBank.feedback.repositories.RefreshTokenRepository;
import com.ccaBank.feedback.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh-expiration-time}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public RefreshToken createRefreshToken(User user) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByUser(user)
                .orElse(new RefreshToken());

        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(
                Instant.now().plusMillis(refreshTokenDurationMs)
        );

        return refreshTokenRepository.save(refreshToken);
    }



    public RefreshToken getByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() ->
                        new TokenRefreshException(token, "Refresh token inexistant"));
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(
                    token.getToken(),
                    "Refresh token expiré"
            );
        }
        return token;
    }
}
