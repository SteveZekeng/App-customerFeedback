package com.ccaBank.feedback.controllers;

import com.ccaBank.feedback.dtos.ServiceBancaireDto;
import com.ccaBank.feedback.services.ServiceBancaireService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gestionFA/serviceBancaire")
public class ServiceBancaireController {

    private final ServiceBancaireService serviceBancaireService;
    public ServiceBancaireController(ServiceBancaireService serviceB) {
        this.serviceBancaireService = serviceB;
    }

    @PostMapping
    public ServiceBancaireDto addService(@RequestBody ServiceBancaireDto serviceBancaireDto) {
        return serviceBancaireService.createServiceBancaire(serviceBancaireDto);
    }

    @GetMapping
    public List<ServiceBancaireDto> getAllServiceBancaire() {
        return serviceBancaireService.getAllServiceBancaire();
    }

    @GetMapping("/{id}")
    public ServiceBancaireDto getServiceBancaire(@PathVariable Long id) {
        return serviceBancaireService.findServiceBancaireById(id);
    }

    @PutMapping("/{id}")
    public ServiceBancaireDto updateServiceB(@PathVariable Long id,
                                             @RequestBody ServiceBancaireDto serviceBancaireDto) {
        return serviceBancaireService.updateServiceBancaire(serviceBancaireDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteServiceBancaire(@PathVariable Long id) {
        serviceBancaireService.deleteServiceBancaire(id);
    }


}
