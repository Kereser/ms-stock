package com.emazon.ms_stock.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class ItemsReqDTO {

    @NotNull
    @Size(min = 1)
    private Set<@Valid ItemQuantityDTO> items;
}
