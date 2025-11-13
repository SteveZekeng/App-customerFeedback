package com.ccaBank.feedback.repositories;

import com.ccaBank.feedback.entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    Optional<Staff> findByMatricule(String matricule);

}
