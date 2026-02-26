package com.ccaBank.feedback.services;

import com.ccaBank.feedback.dtos.*;
import com.ccaBank.feedback.entities.*;
import com.ccaBank.feedback.exceptions.NosuchExistException;
import com.ccaBank.feedback.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RDVService {

    private final RDVRepository rdvRepository;
    private final ModelMapper modelMapper;
    private final ClientRepository clientRepository;
    private final AgenceRepository agenceRepository;
    private final ServiceBancaireRepository serviceBRepository;
    private final UserRepository userRepository;
    private final MailService mailService;
    private final StaffRepository staffRepository;

    public RDVService(RDVRepository rdvRepository,ModelMapper modelMapper, ClientRepository
                      clientRepository, AgenceRepository agenceRepository,
                      ServiceBancaireRepository serviceBRepository,
                      UserRepository userRepository, MailService mailService,
            StaffRepository staffRepository){
        this.rdvRepository = rdvRepository;
        this.modelMapper = modelMapper;
        this.clientRepository = clientRepository;
        this.agenceRepository = agenceRepository;
        this.serviceBRepository = serviceBRepository;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.staffRepository = staffRepository;
    }

    public RDVDto mapToDto(RDV rdv) {
        RDVDto RdvDto = modelMapper.map(rdv, RDVDto.class);

        RdvDto.setStaffMatricule(
                rdv.getStaff() != null ? rdv.getStaff().getMatricule() : null
        );

        if (rdv.getAgence() != null) {
            AgenceDto agenceDto = modelMapper.map(rdv.getAgence(), AgenceDto.class);
            RdvDto.setAgence_Id(agenceDto);
        }

        if (rdv.getServiceBancaire() != null) {
            ServiceBancaireDto serviceBDto = modelMapper.map(rdv.getServiceBancaire(),
                    ServiceBancaireDto.class);
            RdvDto.setServiceBancaire_Id(serviceBDto);
        }

        if (rdv.getClient() != null) {
            ClientDto clientDto = modelMapper.map(rdv.getClient(), ClientDto.class);
            RdvDto.setClient_Id(clientDto);
        }
        return RdvDto;
    }

    public RDV mapToEntity(RDVDto rdvDto) {
        return modelMapper.map(rdvDto, RDV.class);
    }

    public RDVDto createRDV(RDVDto rdvDto, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Client client = clientRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        RDV rdv = mapToEntity(rdvDto);

        if (rdvDto.getReferenceNumber() == null) {
            rdv.setReferenceNumber("RDV-" +
                    UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        } else {
            rdv.setReferenceNumber(rdvDto.getReferenceNumber());
        }

        rdv.setClient(client);
        rdv.setStatutRDV(StatutRDV.EN_ATTENTE);
        rdv.setDateHeure(rdvDto.getDateHeure());

        if (rdvDto.getAgence_Id() != null) {
            Agence agence = agenceRepository.findById(rdvDto.getAgence_Id().getId())
                    .orElseThrow(() -> new NosuchExistException("agence introuvable"));
            rdv.setAgence(agence);
        }

        if (rdvDto.getServiceBancaire_Id() != null) {
            ServiceBancaire serviceBancaire = serviceBRepository.findById(rdvDto.
                            getServiceBancaire_Id().getId())
                    .orElseThrow(() -> new NosuchExistException("service bancaire introuvable"));
            rdv.setServiceBancaire(serviceBancaire);
        }

        return mapToDto(rdvRepository.save(rdv));
    }

    public List<RDVDto> getAllRDVs() {
        return rdvRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public RDVDto getRDVForClient(Long rdvId, String username) {
        RDV rdv = rdvRepository.findById(rdvId)
                .orElseThrow(() -> new RuntimeException("RDV introuvable"));

        if (!rdv.getClient().getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Accès interdit");
        }

        return mapToDto(rdv);
    }


    public RDVDto updateRDV(RDVDto rdvDto, Long id) {
        Optional<RDV> existingRdv = rdvRepository.findById(id);
        if(!existingRdv.isPresent()) {
            throw new NosuchExistException("rdv introuvable");
        } else{
            existingRdv.get().setStatutRDV(rdvDto.getStatutRDV());
            existingRdv.get().setReferenceNumber(existingRdv.get().getReferenceNumber());
            existingRdv.get().setDateHeure(existingRdv.get().getDateHeure());
        }
        return mapToDto(rdvRepository.save(existingRdv.get()));
    }

    public void deleteRDV(Long id) {
        Optional<RDV> existingRdv = rdvRepository.findById(id);
        if(!existingRdv.isPresent()) {
            throw new NosuchExistException("rdv introuvable");
        }
        rdvRepository.deleteById(id);
    }
    public List<RDVDto> findRDVsByClientUsername(String username) {
        Client client = clientRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        return rdvRepository.findByClient(client)
                .stream()
                .map(this::mapToDto)

                .toList();
    }


    public void notifyAdminsOfAgence(Long rdvId, String username) {

        RDV rdv = rdvRepository.findById(rdvId)
                .orElseThrow(() -> new NosuchExistException("RDV introuvable"));

        User sender = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Long agenceId = rdv.getAgence().getId();

        List<Staff> staffs = staffRepository.findByAgenceId(agenceId);

        if (staffs.isEmpty()) {
            throw new RuntimeException("Aucun staff trouvé pour cette agence");
        }

        String html = """
        <h2>Nouvelle demande de Rendez-vous</h2>

        <p><b>Client :</b> %s</p>
        <p><b>Email client :</b> %s</p>
        <p><b>Référence RDV :</b> %s</p>
        <p><b>Agence :</b> %s</p>
        <p><b>Service :</b> %s</p>
        <p><b>Date & heure :</b> %s</p>

        <hr>
        <p>
          Cette demande concerne uniquement votre agence.
          Merci de vous connecter au dashboard CCA pour la traiter.
        </p>
        """.formatted(
                sender.getUsername(),
                sender.getClient().getEmail(),
                rdv.getReferenceNumber(),
                rdv.getAgence().getAgenceLocation(),
                rdv.getServiceBancaire().getServiceName(),
                rdv.getDateHeure()
        );

        for (Staff staff : staffs) {
            mailService.sendHtmlMailFromClient(
                    staff.getStaffEmail(),
                    "RDV à valider – " + rdv.getReferenceNumber(),
                    html,
                    sender
            );
        }
    }

    public List<RDVDto> getRDVsForDashboard(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        LocalDateTime startOfDay = LocalDate.now().minusDays(30).atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(30).atTime(23, 59, 59);

        List<StatutRDV> allowedStatuts =
                List.of(StatutRDV.EN_ATTENTE, StatutRDV.CONFIRMER);

        if (user.getRole().equals(Role.ADMIN)) {
            return rdvRepository.findAll().stream()
                    .filter(r ->
                            r.getDateHeure().isAfter(startOfDay) &&
                                    r.getDateHeure().isBefore(endOfDay) &&
                                    allowedStatuts.contains(r.getStatutRDV())
                    )
                    .map(this::mapToDto)
                    .toList();
        }

        Staff staff = staffRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Staff introuvable"));

        return rdvRepository
                .findByAgenceIdAndDateHeureBetweenAndStatutRDVIn(
                        staff.getAgence().getId(),
                        startOfDay,
                        endOfDay,
                        allowedStatuts
                )
                .stream()
                .map(this::mapToDto)
                .toList();
    }


    public RDVDto confirmerRDV(Long rdvId, String username) {

        RDV rdv = rdvRepository.findById(rdvId)
                .orElseThrow(() -> new RuntimeException("RDV introuvable"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Staff staff = staffRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Staff introuvable"));

        rdv.setStatutRDV(StatutRDV.CONFIRMER);
        rdv.setStaff(staff);

        rdvRepository.save(rdv);

        // mail au client
        mailService.sendHtmlMail(
                rdv.getClient().getEmail(),

                "Confirmation du Rendez-vous",
                "<p>Votre RDV a été confirmé par " + staff.getStaffName() + "</p> a la date " + rdv.getDateHeure()
        );

        return mapToDto(rdv);
    }

    public void honorerRDV(Long id) {

        RDV rdv = rdvRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RDV introuvable"));

        if (rdv.getStatutRDV() == StatutRDV.HONNORER) {
            throw new RuntimeException("RDV déjà honoré");
        }

        if (rdv.getStatutRDV() != StatutRDV.CONFIRMER) {
            throw new RuntimeException("Impossible d’honorer un RDV non confirmé");
        }

        rdv.setStatutRDV(StatutRDV.HONNORER);
        rdvRepository.save(rdv);

//        mailService.sendRDVHonoredNotification(rdv);
    }




}
