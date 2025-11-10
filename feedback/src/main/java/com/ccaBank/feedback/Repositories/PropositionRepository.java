package com.ccaBank.feedback.repositories;

import com.ccaBank.feedback.entities.Proposition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropositionRepository extends JpaRepository<Proposition, Long> {
}
