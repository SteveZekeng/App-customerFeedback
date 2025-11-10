package com.ccaBank.feedback.Dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDto {

    @NotNull
    private String customerName;

    @NotNull
    private String customerPhone;

    private String comment;

    @NotNull
    private Long staff_id;
}
