package com.ccaBank.feedback.repositories;

import com.ccaBank.feedback.entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    Optional<Staff> findByMatricule(String matricule);

    List<Staff> findByAgenceId(Long agenceId);

//    @Query("SELECT s.matricule, s.staffName, AVG(r.value) as averageScore FROM Staff s, " +
//            "Feedback f, Response r WHERE s.agence.id = :agenceId " +
//            "GROUP BY s.matricule, s.staffName ORDER BY averageScore DESC ")
//    List<Object[]> findBestStaffInAgence(@Param("agenceId") Long agenceId);


}
