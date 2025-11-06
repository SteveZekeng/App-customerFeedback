package com.ccaBank.feedback.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Table(name = "propositions")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Proposition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Item items;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;


}
