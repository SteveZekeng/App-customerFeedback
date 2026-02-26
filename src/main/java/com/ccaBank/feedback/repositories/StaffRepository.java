package com.ccaBank.feedback.repositories;

import com.ccaBank.feedback.entities.Staff;
import com.ccaBank.feedback.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    Optional<Staff> findByMatricule(String matricule);

    List<Staff> findByAgenceId(@Param("agenceId")Long agenceId);

    List<Staff> findStaffByAgence_AgenceLocation(String agenceAgenceLocation);

    Optional<Staff> findByUser(User user);


    Optional<Staff> findByUserUsername(String username);
}
