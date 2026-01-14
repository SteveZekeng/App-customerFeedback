package com.ccaBank.feedback.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RDV {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "numero_reference", unique = true)
    private String referenceNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_RDV")
    private StatutRDV statutRDV;

    @Column(nullable = false, name = "date_et_heure")
    private LocalDateTime date_heure;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceBancaire serviceBancaire;

    @ManyToOne
    @JoinColumn(name = "agence_id")
    private Agence agence;

}
