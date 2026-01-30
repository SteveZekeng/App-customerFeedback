package com.ccaBank.feedback.repositories;

import com.ccaBank.feedback.dtos.StatsGlobalRDVAgenceDto;
import com.ccaBank.feedback.entities.Client;
import com.ccaBank.feedback.entities.RDV;
import com.ccaBank.feedback.entities.StatutRDV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RDVRepository extends JpaRepository<RDV, Long> {

    Optional<RDV> findRDVByAgence_AgenceLocation(String agenceAgenceLocation);

    List<RDV> findRDVByClient_User_Username(String username);

    Optional<RDV> findRDVByServiceBancaire_ServiceName(String serviceName);

    List<RDV> findByClient(Client client);

    List<RDV> findByAgenceId(Long agenceId);

    List<RDV> findByAgenceIdAndDateHeureBetweenAndStatutRDVIn(
            Long agenceId,
            LocalDateTime start,
            LocalDateTime end,
            List<StatutRDV> statuts
    );

    @Query("""
            SELECT COUNT(r) FROM RDV r WHERE r.agence.id = :agenceId""")
    Long countByAgence(@Param("agenceId") Long agenceId);

    @Query("""
    SELECT COUNT(r) FROM RDV r WHERE r.agence.id = :agenceId
         AND r.statutRDV = :statut""")
    Long countByAgenceAndStatut(
            @Param("agenceId") Long agenceId,
            @Param("statut") StatutRDV statut
    );

    @Query("""
    SELECT new com.ccaBank.feedback.dtos.StatsGlobalRDVAgenceDto(a.agenceLocation, COUNT(r))
        FROM RDV r JOIN r.agence a GROUP BY a.agenceLocation""")
    List<StatsGlobalRDVAgenceDto> countRDVByAgence();


}
