package com.ccaBank.feedback.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "staff")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String matricule;

    @Column(length = 50, nullable = false)
    private String staffName;

    @Column(nullable = false, unique = true)
    private String staffPhone;

    @Column(nullable = false, unique = true)
    private String staffEmail;

    @OneToMany(mappedBy = "staff", fetch = FetchType.EAGER,  cascade = CascadeType.ALL)
    private List<Feedback> feedback = new ArrayList<>();

//    @OneToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;

    @ManyToOne
    @JoinColumn(name = "agence_id")
    private Agence agence;

    public double getAverage(){
        return feedback.stream().mapToDouble(Feedback::getAverage).average().orElse(0.0);
    }


}
