package com.emazon.ms_stock.application.dto.input;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemReqDTO {

    @NotNull
    @Positive
    private Long articleId;

    @NotNull
    @Positive
    private Long quantity;
}
