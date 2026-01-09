package com.ccaBank.feedback.repositories;

import com.ccaBank.feedback.entities.RefreshToken;
import com.ccaBank.feedback.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);

    int deleteByUser(User user);
}
