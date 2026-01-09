package com.ccaBank.feedback.dtos;

import com.ccaBank.feedback.entities.Question;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull @Size(min=9, max=9)
    private String customerPhone;

    private String comment;

    private StaffDto staff_id;

    private List<ResponseDto> responses;

    private List<QuestionDto> questions;

}
