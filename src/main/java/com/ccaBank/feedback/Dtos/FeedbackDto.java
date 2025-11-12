package com.ccaBank.feedback.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDto {

    @NotNull
    private String customerName;

    @NotNull
    private String customerPhone;

    private String comment;

    private Long staff_id;

    private List<ResponseDto> responses;
}
