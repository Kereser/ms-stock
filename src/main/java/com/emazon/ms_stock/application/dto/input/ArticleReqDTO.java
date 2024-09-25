package com.emazon.ms_stock.application.dto.input;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class ArticleReqDTO {
    @NotBlank
    @Size(min = 3, max = 50)
    private String name;
    @NotBlank
    @Size(min = 5, max = 240)
    private String description;
    @NotNull
    @Positive
    private BigDecimal price;
    @NotNull
    @PositiveOrZero
    private Long quantity;
    @NotNull
    @Size(min = 1, max = 3)
    private Set<Long> categoryIds;
    @NotNull
    @Positive
    private Long brandId;
}
