package com.ccaBank.feedback.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(length = 100, nullable = false, unique = true)
    private String staffId;

    @Column(length = 50, nullable = false)
    private String staffName;

    @Column(nullable = false, unique = true)
    private String staffPhone;

    @Column(nullable = false, unique = true)
    private String staffEmail;

    @OneToMany(mappedBy = "staff", fetch = FetchType.EAGER,  cascade = CascadeType.ALL)
    private List<Feedback> feedback;


}
