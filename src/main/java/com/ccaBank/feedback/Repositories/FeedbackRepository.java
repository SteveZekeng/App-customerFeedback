package com.ccaBank.feedback.Repositories;

import com.ccaBank.feedback.Dtos.ResponseDto;
import com.ccaBank.feedback.Entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {


    List<ResponseDto> findByFeedbackId(Long feedbackId);



}
