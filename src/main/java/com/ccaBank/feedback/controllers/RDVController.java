package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.RDVDto;
import com.ccaBank.feedback.services.RDVService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gestionFA/RDV")
public class RDVController {

    private final RDVService rdvService;
    public RDVController(RDVService rdvService) {
        this.rdvService = rdvService;
    }

    @PostMapping
    public RDVDto addRDV(@RequestBody RDVDto rdvDto) {
        return rdvService.createRDV(rdvDto);
    }

    @GetMapping
    public List<RDVDto> getAllRDVs() {
        return rdvService.getAllRDVs();
    }

    @GetMapping("/{id}")
    public RDVDto getRDV(@PathVariable Long id) {
        return rdvService.getRDVById(id);
    }

    @PutMapping("/{id}")
    public RDVDto updateRDV(@RequestBody RDVDto rdvDto, @PathVariable Long id) {
        return rdvService.updateRDV(rdvDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteRDV(@PathVariable Long id) {
        rdvService.deleteRDV(id);
    }
}
