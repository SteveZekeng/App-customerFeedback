package com.ccaBank.feedback.repositories;

import com.ccaBank.feedback.dtos.FeedbackDto;
import com.ccaBank.feedback.entities.Feedback;
import com.ccaBank.feedback.entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    List<Staff> findByStaffId(String staffId);

    @Query("SELECT f FROM Feedback f WHERE f.staff.staffId = :staffId")
    List<Feedback> getAllFeedbackByStaffId(@Param("staffId") String staffId);

}
