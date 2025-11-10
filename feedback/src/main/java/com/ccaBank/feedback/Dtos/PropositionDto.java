package com.ccaBank.feedback.Dtos;

import com.ccaBank.feedback.Entities.Item;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropositionDto {

    @NotNull
    private Item  item;

    @NotNull
    private Long question_id;
}
