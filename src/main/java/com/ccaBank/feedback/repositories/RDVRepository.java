package com.ccaBank.feedback.repositories;

import com.ccaBank.feedback.entities.RDV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RDVRepository extends JpaRepository<RDV, Long> {

    Optional<RDV> findRDVByAgence_AgenceLocation(String agenceAgenceLocation);

    Optional<RDV> findRDVByClient_FirstName(String firstName);

    Optional<RDV> findRDVByServiceBancaire_ServiceName(String serviceName);
}
