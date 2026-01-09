package com.ccaBank.feedback.repositories;

import com.ccaBank.feedback.entities.Agence;
import com.ccaBank.feedback.entities.RDV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgenceRepository extends JpaRepository<Agence, Long> {

    Optional<Agence> findByAgenceLocation(String agenceLocation);



}
