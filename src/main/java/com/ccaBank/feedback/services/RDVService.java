package com.ccaBank.feedback.services;

import com.ccaBank.feedback.dtos.AgenceDto;
import com.ccaBank.feedback.dtos.ClientDto;
import com.ccaBank.feedback.dtos.RDVDto;
import com.ccaBank.feedback.dtos.ServiceBancaireDto;
import com.ccaBank.feedback.entities.Agence;
import com.ccaBank.feedback.entities.Client;
import com.ccaBank.feedback.entities.RDV;
import com.ccaBank.feedback.entities.ServiceBancaire;
import com.ccaBank.feedback.exceptions.NosuchExistException;
import com.ccaBank.feedback.repositories.AgenceRepository;
import com.ccaBank.feedback.repositories.ClientRepository;
import com.ccaBank.feedback.repositories.RDVRepository;
import com.ccaBank.feedback.repositories.ServiceBancaireRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RDVService {

    private final RDVRepository rdvRepository;
    private final ModelMapper modelMapper;
    private final ClientRepository clientRepository;
    private final AgenceRepository agenceRepository;
    private final ServiceBancaireRepository serviceBRepository;

    public RDVService(RDVRepository rdvRepository,ModelMapper modelMapper, ClientRepository
                      clientRepository, AgenceRepository agenceRepository,
                      ServiceBancaireRepository serviceBRepository) {
        this.rdvRepository = rdvRepository;
        this.modelMapper = modelMapper;
        this.clientRepository = clientRepository;
        this.agenceRepository = agenceRepository;
        this.serviceBRepository = serviceBRepository;
    }

    public RDVDto mapToDto(RDV rdv) {
        RDVDto rdvDto = modelMapper.map(rdv, RDVDto.class);

        if(rdv.getAgence() != null) {
            AgenceDto agenceDto = modelMapper.map(rdv.getAgence(), AgenceDto.class);
            rdvDto.setAgence_id(agenceDto);
        }

        if(rdv.getClient() != null) {
            ClientDto clientDto = modelMapper.map(rdv.getClient(), ClientDto.class);
            rdvDto.setClient_id(clientDto);
        }

        if(rdv.getServiceBancaire() != null) {
            ServiceBancaireDto serviceBancaireDto =  modelMapper.map(rdv.getServiceBancaire(), ServiceBancaireDto.class);
            rdvDto.setServiceBancaire_id(serviceBancaireDto);
        }
        return rdvDto;
    }

    public RDV mapToEntity(RDVDto rdvDto) {
        return modelMapper.map(rdvDto, RDV.class);
    }

    public RDVDto createRDV(RDVDto rdvDto) {
        RDV rdv = mapToEntity(rdvDto);

        rdv.setStatutRDV(rdvDto.getStatutRDV());
        rdv.setDate_heure(rdvDto.getDate_heure());
        rdv.setReferenceNumber(rdvDto.getReferenceNumber());

        if(rdv.getAgence() != null) {
            Agence agence = agenceRepository.findById(rdvDto.getAgence_id().getId())
                    .orElseThrow(() -> new NosuchExistException("agence introuvable"));
            rdv.setAgence(agence);
        }

        if(rdv.getClient() != null) {
            Client client = clientRepository.findById(rdvDto.getClient_id().getId())
                    .orElseThrow(() -> new NosuchExistException("client introuvable"));
            rdv.setClient(client);
        }

        if(rdv.getServiceBancaire() != null) {
            ServiceBancaire serviceBancaire = serviceBRepository.findById(rdvDto.getServiceBancaire_id().getId())
                    .orElseThrow(() -> new NosuchExistException("serviceBancaire introuvable"));
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

    public RDVDto getRDVById(Long id) {
        RDV rdv;
        try {
            rdv = rdvRepository.findById(id)
                    .orElseThrow(() -> new NosuchExistException("rdv introuvable"));
        } catch (NosuchExistException e) {
            System.err.println("Erreur lors de la recherche :" + e.getMessage());
            throw e;
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
            existingRdv.get().setDate_heure(existingRdv.get().getDate_heure());
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


}
