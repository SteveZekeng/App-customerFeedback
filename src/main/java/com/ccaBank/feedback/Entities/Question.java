package com.ccaBank.feedback.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "questions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String labelQuestion;

    @Enumerated(EnumType.STRING)
    private InputType inputType;

    @OneToMany(mappedBy = "question",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Proposition> proposition;

    @OneToMany(mappedBy = "question",   cascade = CascadeType.ALL)
    private List<Response> response;

}
