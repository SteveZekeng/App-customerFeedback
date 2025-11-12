package com.ccaBank.feedback.services;

import com.ccaBank.feedback.dtos.FeedbackDto;
import com.ccaBank.feedback.dtos.StaffDto;
import com.ccaBank.feedback.entities.Staff;
import com.ccaBank.feedback.exceptions.NosuchExistException;
import com.ccaBank.feedback.repositories.StaffRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffService {

    private final StaffRepository staffRepository;
    private final ModelMapper modelMapper;

    public StaffService(StaffRepository staffRepository, ModelMapper modelMapper) {
        this.staffRepository = staffRepository;
        this.modelMapper = modelMapper;
    }

    private StaffDto mapToDto(Staff staff) {
        StaffDto staffDto = modelMapper.map(staff, StaffDto.class);
        return staffDto;
    }

    private Staff mapToEntity(StaffDto staffDto) {
        return modelMapper.map(staffDto, Staff.class);
    }

    public StaffDto createStaff(StaffDto staffDto) {
        Staff staff = mapToEntity(staffDto);
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

    @Transactional
    public boolean deleteStaff(Long id) {
        if (staffRepository.existsById(id)) {
            staffRepository.deleteById(id);
            return true;
        }
            return false;

    }

}
