package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.ClientDto;
import com.ccaBank.feedback.dtos.StaffDto;
import com.ccaBank.feedback.entities.*;
import com.ccaBank.feedback.services.StaffService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customFeedback/staff")
@Tag(name = "Staff", description = "Staff REST API")

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

    @Operation(summary = "Liste des staffs")
    @GetMapping
    public Page<StaffDto> getAllStaffs(Pageable pageable) {

        return staffService.findAllStaffs(pageable);
    }

    @Operation(summary = "Retouner les details d'un staff")
    @GetMapping("/{id}")
    public StaffDto getStaffById(@PathVariable("id") Long id){

        return staffService.findStaffById(id);
    }

    @Operation(summary = "Mise a jour d'un staff")
    @PutMapping("/{id}")
    public StaffDto updateStaffById(@PathVariable("id") Long id,
                                              @RequestBody StaffDto staffDto){
        return staffService.updateStaff(id, staffDto);
    }

    @Operation(summary = "Suprression d'un staff")
    @DeleteMapping("/{id}")
    public void deleteStaffById(@PathVariable("id") Long id){
       staffService.deleteStaff(id);
    }

    @Operation(summary = "Liste des staffs par identifiant d'agence")
    @GetMapping("/staffAgence/{agenceId}")
    public List<StaffDto> getAllByAgenceId(@PathVariable("agenceId") Long agenceId){
        return staffService.staffByAgenceId(agenceId);
    }

    @Operation(summary = "Liste des staffs par scoring decroissant", description = "Deroule une " +
            "liste des staffs classés par ordre decroissant en fonction de leur score moyen de tous " +
            "leur feedback")
    @GetMapping("/listStaffOrderDesc")
    public List<StaffDto> getListStaffOrder() {
        return staffService.listStaffOrder();
    }

    @Operation(summary = "Liste des staffs d'une agence")
    @GetMapping("/listStaffByAgence")
    public List<StaffDto> getListStaffByAgenceLocation(String agenceLocation){
        return staffService.listStaffByAgenceLocation(agenceLocation);
    }
}

