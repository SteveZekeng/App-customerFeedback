package com.ccaBank.feedback.repositories;

import com.ccaBank.feedback.entities.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {

    List<Response> findByFeedbackId(@Param("feedbackId")Long feedbackId);

}
