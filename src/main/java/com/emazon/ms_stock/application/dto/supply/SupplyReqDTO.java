package com.emazon.ms_stock.application.dto.supply;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class SupplyReqDTO {

    @NotNull
    @Positive
    private Long articleId;
    @NotNull
    @Positive
    private Long quantity;
}
