package com.ccaBank.feedback.dtos;

import com.ccaBank.feedback.entities.Question;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDto {

    private Long id;

    @NotNull
    private String customerName;

    @NotNull
    private String customerPhone;

    private String comment;

    private Long staff_id;

    private List<ResponseDto> responses;

    private List<QuestionDto> questions;

}
