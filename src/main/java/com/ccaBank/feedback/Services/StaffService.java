package com.ccaBank.feedback.services;

import com.ccaBank.feedback.dtos.StaffDto;
import com.ccaBank.feedback.entities.Agence;
import com.ccaBank.feedback.entities.Staff;
import com.ccaBank.feedback.exceptions.NosuchExistException;
import com.ccaBank.feedback.repositories.AgenceRepository;
import com.ccaBank.feedback.repositories.StaffRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StaffService {

    private final StaffRepository staffRepository;
    private final AgenceRepository agenceRepository;
    private final ModelMapper modelMapper;

    public StaffService(StaffRepository staffRepository, ModelMapper modelMapper,
                        AgenceRepository agenceRepository) {
        this.staffRepository = staffRepository;
        this.agenceRepository = agenceRepository;
        this.modelMapper = modelMapper;
    }

    private StaffDto mapToDto(Staff staff) {
        StaffDto staffDto = modelMapper.map(staff, StaffDto.class);

        if (staff.getAgence() != null) {
            Agence agenceDto = staff.getAgence();
            staffDto.setAgence_id(agenceDto.getId());
        }
        return staffDto;
    }

    private Staff mapToEntity(StaffDto staffDto) {
        return modelMapper.map(staffDto, Staff.class);
    }

    public StaffDto createStaff(StaffDto staffDto) {
        Staff staff = mapToEntity(staffDto);

        staff.setMatricule(staffDto.getMatricule());
        staff.setStaffName(staffDto.getStaffName());
        staff.setStaffEmail(staffDto.getStaffEmail());
        staff.setStaffPhone(staffDto.getStaffPhone());

        if (staffDto.getAgence_id() != null) {
            Agence agence = agenceRepository.findById(staffDto.getAgence_id()).orElseThrow(() ->
                    new NosuchExistException("agence introuvable"));
            staff.setAgence(agence);
        }
        return mapToDto(staffRepository.save(staff));
    }

    public List<StaffDto> findAllStaffs() {
        return staffRepository.findAll()
                .stream().map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public StaffDto findStaffById(Long id) {
        Staff staff;
        try {
            staff = staffRepository.findById(id)
                    .orElseThrow(() -> new NosuchExistException("staff introuvable ou inexistant"));
        } catch (NosuchExistException e){
            System.err.println("Erreur lors de la recherche :" + e.getMessage());
            throw e;
        }
        return mapToDto(staff);
    }

    public StaffDto updateStaff(Long id, StaffDto staffDto) {
        Optional<Staff> existingStaff = staffRepository.findById(id);
        if (!existingStaff.isPresent()) {
            throw new NosuchExistException("staff introuvable ou inexistant");
        }else {
            existingStaff.get().setStaffName(staffDto.getStaffName());
            existingStaff.get().setStaffEmail(staffDto.getStaffEmail());
            existingStaff.get().setStaffPhone(staffDto.getStaffPhone());
        }
        return mapToDto(staffRepository.save(existingStaff.get()));
    }

    public void deleteStaff(Long id){
        Optional<Staff> existingStaff = staffRepository.findById(id);
        if (!existingStaff.isPresent()) {
            throw new NosuchExistException("staff introuvable ou inexistant");
        }
        staffRepository.deleteById(id);
        log.info("staff deleted successfully");
    }

    public List<StaffDto> staffByAgenceId(Long agenceId) {
        Optional<Agence> agence = agenceRepository.findById(agenceId);
        if(!agence.isPresent()) {
            throw new NosuchExistException("agence introuvable ou inexistante");
        }
        return staffRepository.findByAgenceId(agenceId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

}
