package com.ccaBank.feedback.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "propositions")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Proposition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;
    private double score;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;


}
