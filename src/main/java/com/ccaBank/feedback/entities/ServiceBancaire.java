package com.ccaBank.feedback.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "service_bancaire")
public class ServiceBancaire {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "service")
    private String serviceName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "categorie")
    private Category category;

    @OneToMany(mappedBy = "serviceBancaire", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RDV> rdvList;
}
