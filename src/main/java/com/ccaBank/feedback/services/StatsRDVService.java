package com.ccaBank.feedback.services;

import com.ccaBank.feedback.dtos.AgenceDto;
import com.ccaBank.feedback.dtos.StatsGlobalRDVAgenceDto;
import com.ccaBank.feedback.dtos.StatsRDVAgenceDto;
import com.ccaBank.feedback.entities.Staff;
import com.ccaBank.feedback.entities.StatutRDV;
import com.ccaBank.feedback.repositories.RDVRepository;
import com.ccaBank.feedback.repositories.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatsRDVService {

    private final RDVRepository rdvRepository;
    private final StaffRepository staffRepository;

    public StatsRDVService (RDVRepository rdvRepository, StaffRepository staffRepository) {
        this.rdvRepository = rdvRepository;
        this.staffRepository = staffRepository;
    }

    public StatsRDVAgenceDto getStatsForAgenceOfUser(String username) {

        Staff staff = staffRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Staff introuvable"));

        Long agenceId = staff.getAgence().getId();

        return new StatsRDVAgenceDto(
                staff.getAgence().getAgenceLocation(),
                rdvRepository.countByAgence(agenceId),
                rdvRepository.countByAgenceAndStatut(agenceId, StatutRDV.EN_ATTENTE),
                rdvRepository.countByAgenceAndStatut(agenceId, StatutRDV.CONFIRMER),
                rdvRepository.countByAgenceAndStatut(agenceId, StatutRDV.HONNORER)
        );
    }

    public List<StatsGlobalRDVAgenceDto> getGlobalStats() {
        return rdvRepository.countRDVByAgence();
    }

}
