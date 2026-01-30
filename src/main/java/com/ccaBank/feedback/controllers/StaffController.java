package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.ClientDto;
import com.ccaBank.feedback.dtos.StaffDto;
import com.ccaBank.feedback.entities.Client;
import com.ccaBank.feedback.entities.Role;
import com.ccaBank.feedback.entities.Staff;
import com.ccaBank.feedback.entities.User;
import com.ccaBank.feedback.services.StaffService;


import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customFeedback/staff")

public class StaffController {

    private final StaffService staffService;
    private final PasswordEncoder passwordEncoder;

    public StaffController(StaffService staffService, PasswordEncoder passwordEncoder) {
    this.staffService = staffService;
    this.passwordEncoder = passwordEncoder;
    }

//    @PostMapping
//    public ResponseEntity<?> registerStaff(@Valid @RequestBody StaffDto dto) {
//
//        User user = new User();
//        user.setUsername(dto.getUsername());
////        user.setEmail(dto.getEmail());
//        user.setPassword(passwordEncoder.encode(dto.getPassword()));
//        user.setRole(Role.ROLE_STAFF);
//
//        user = staffService.save(user);
//
//        Staff staff = new Staff();
//        staff.setStaffName(dto.getStaffName());
//        staff.setStaffEmail(dto.getStaffEmail());
//        staff.setStaffPhone(dto.getStaffPhone());
//        staff.setMatricule(dto.getMatricule());
//        staff.setUser(user);
//
//        clientRepository.save(client);
//
//        return ResponseEntity.ok("Client enregistré avec succès");
//    }

    @GetMapping
    public Page<StaffDto> getAllStaffs(Pageable pageable) {

        return staffService.findAllStaffs(pageable);
    }

    @GetMapping("/{id}")
    public StaffDto getStaffById(@PathVariable("id") Long id){

        return staffService.findStaffById(id);
    }

    @PutMapping("/{id}")
    public StaffDto updateStaffById(@PathVariable("id") Long id,
                                              @RequestBody StaffDto staffDto){
        return staffService.updateStaff(id, staffDto);
    }

    @DeleteMapping("/{id}")
    public void deleteStaffById(@PathVariable("id") Long id){
       staffService.deleteStaff(id);
    }

    @GetMapping("/staffAgence/{agenceId}")
    public List<StaffDto> getAllByAgenceId(@PathVariable("agenceId") Long agenceId){
        return staffService.staffByAgenceId(agenceId);
    }

    @GetMapping("/listStaffOrderDesc")
    public List<StaffDto> getListStaffOrder() {
        return staffService.listStaffOrder();
    }

    @GetMapping("/listStaffByAgence")
    public List<StaffDto> getListStaffByAgenceLocation(String agenceLocation){
        return staffService.listStaffByAgenceLocation(agenceLocation);
    }
}

