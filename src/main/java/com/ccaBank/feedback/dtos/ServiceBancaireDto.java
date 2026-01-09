package com.ccaBank.feedback.dtos;

import com.ccaBank.feedback.entities.Category;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceBancaireDto {

    private Long id;

    @NotNull
    private String serviceName;

    @NotNull
    private Category category;
}
