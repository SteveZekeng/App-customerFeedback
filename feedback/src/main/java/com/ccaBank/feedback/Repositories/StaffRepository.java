package com.ccaBank.feedback.Repositories;

import com.ccaBank.feedback.Entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    Optional<Staff> findByStaffId(String staffId);
    Optional<Staff> findByStaffName(String staffName);
}
