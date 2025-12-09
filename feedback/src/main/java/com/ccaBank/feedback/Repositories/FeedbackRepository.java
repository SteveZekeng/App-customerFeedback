package com.ccaBank.feedback.repositories;

import com.ccaBank.feedback.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {


    Optional<Feedback> findByResponseId(Long responseId);

}
