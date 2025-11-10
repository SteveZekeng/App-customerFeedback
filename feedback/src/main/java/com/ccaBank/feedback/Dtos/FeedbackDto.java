package com.ccaBank.feedback.Dtos;

import com.ccaBank.feedback.Entities.Customer;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDto {

    @NotNull
    private Customer customer;

    private String comment;

    @NotNull
    private Long staff_id;
}
