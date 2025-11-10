package com.ccaBank.feedback.Dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {

    private int value;
    private String selectedLabel;

    @NotNull
    private Long question_id;

    @NotNull
    private Long feedback_id;
}
