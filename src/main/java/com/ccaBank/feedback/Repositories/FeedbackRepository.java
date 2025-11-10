package com.ccaBank.feedback.repositories;

import com.ccaBank.feedback.dtos.ResponseDto;
import com.ccaBank.feedback.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<ResponseDto> findResponseById(Long feedbackId);



}
