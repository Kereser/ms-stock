package com.emazon.ms_stock.application.dto.out;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CategoryReqDTO {

    @NotBlank
    @Size(max = 50, min = 3)
    private String name;

    @NotBlank
    @Size(max = 90, min = 3)
    private String description;
}
