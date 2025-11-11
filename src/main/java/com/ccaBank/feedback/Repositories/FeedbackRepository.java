package com.ccaBank.feedback.repositories;

import com.ccaBank.feedback.dtos.ResponseDto;
import com.ccaBank.feedback.entities.Feedback;
import com.ccaBank.feedback.entities.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query("SELECT r FROM Response r WHERE r.feedback.id = :feedbackId")
    List<Response> ResponsesByFeedbackId(@Param("feedbackId") Long feedbackId);




}
