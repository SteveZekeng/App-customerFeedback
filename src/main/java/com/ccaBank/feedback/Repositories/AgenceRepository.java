package com.ccaBank.feedback.repositories;

import com.ccaBank.feedback.dtos.AgenceDto;
import com.ccaBank.feedback.entities.Agence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgenceRepository extends JpaRepository<Agence, Long> {

    @Query("SELECT AVG(r.value) FROM Response r " +
            "WHERE r.feedback.staff.agence.id = :agenceId")
    Double findAverageScoreByAgenceId(@Param("agenceId") Long agenceId);

    @Query("SELECT new com.ccaBank.feedback.dtos.AgenceDto( a.agenceCity, " +
            "a.agenceLocation, AVG(r.value)) FROM Response r JOIN r.feedback f " +
            "JOIN f.staff s JOIN s.agence a GROUP BY a.agenceCity, a.agenceLocation " +
            "ORDER BY AVG(r.value) DESC")
    List<AgenceDto> findAgenceOrder();

}
