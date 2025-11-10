package com.ccaBank.feedback.Dtos;

import com.ccaBank.feedback.Entities.InputType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto {

    @NotNull
    private String labelQuestion;

    @NotNull
    private InputType inputType;

    @NotNull
    private List<PropositionDto> propositions;

}
