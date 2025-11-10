package com.ccaBank.feedback.Controller;

import com.ccaBank.feedback.Dtos.FeedbackDto;
import com.ccaBank.feedback.Dtos.StaffDto;
import com.ccaBank.feedback.Services.StaffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> addStaff(@RequestBody StaffDto staffDto){
        staffService.createStaff(staffDto);
        return  new ResponseEntity<>("staff created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public List<StaffDto> getAllStaffs(){

        return staffService.findAllStaffs();
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
    public ResponseEntity<String> deleteStaffById(@PathVariable Long id){
        staffService.deleteStaff(id);
        return ResponseEntity.ok("Staff deleted successfully");
    }

    @GetMapping("/{staffId}")
    public List<FeedbackDto> listFeedbackByStaffId(@PathVariable("staffId") String staffId){
        return staffService.selectFeedbackByStaffId(staffId);
    }
}

