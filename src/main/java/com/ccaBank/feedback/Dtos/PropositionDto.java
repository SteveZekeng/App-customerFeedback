package com.ccaBank.feedback.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropositionDto {

    private String label;
    private int score;
    private Long question_id;
}
