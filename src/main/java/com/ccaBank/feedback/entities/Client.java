package com.ccaBank.feedback.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "nom et prenom")
    private String firstName;

    @Column(nullable = false, name = "telephone", unique = true)
    private String phone;

    @Column(nullable = false)
    private String ville;

    @Column(name = "numero_de_compte")
    private String numeroCompte;

    @Column(nullable = false)
    private String email;


    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RDV> rdvList;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

