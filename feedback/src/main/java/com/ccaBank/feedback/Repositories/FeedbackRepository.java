package com.ccaBank.feedback.Repositories;

import com.ccaBank.feedback.Entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {


    Optional<Feedback> findByResponseId(Long responseId);

}
