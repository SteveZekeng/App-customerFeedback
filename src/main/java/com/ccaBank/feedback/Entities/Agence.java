package com.ccaBank.feedback.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agences")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Agence {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String agenceMatriculate;

    @Column(nullable = false)
    private String agenceCity;

    @Column(nullable = false)
    private String agenceLocation;

    @OneToMany(mappedBy = "agence", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Staff> staffs = new ArrayList<>();

    public double getAverage(){
        return staffs.stream().mapToDouble(Staff::getAverage).average().orElse(0.0);
    }
}
