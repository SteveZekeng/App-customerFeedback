package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.StaffDto;
import com.ccaBank.feedback.services.StaffService;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customFeedback/staff")

public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
    this.staffService = staffService;
    }

    @PostMapping
    public StaffDto addStaff(@RequestBody StaffDto staffDto){

        return  staffService.createStaff(staffDto);
    }

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

