package com.ccaBank.feedback.repositories;

import com.ccaBank.feedback.dtos.FeedbackDto;
import com.ccaBank.feedback.entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    List<Staff> findByStaffId(String staffId);
    List<FeedbackDto> getAllFeedbackByStaffId(String staffId);
}
