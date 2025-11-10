package com.ccaBank.feedback.Repositories;

import com.ccaBank.feedback.Dtos.FeedbackDto;
import com.ccaBank.feedback.Entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    List<Staff> findByStaffId(String staffId);
    List<FeedbackDto> getAllFeedbackByStaffId(String staffId);
}
