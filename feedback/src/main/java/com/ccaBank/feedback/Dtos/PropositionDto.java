package com.ccaBank.feedback.dtos;

import com.ccaBank.feedback.entities.Item;
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
