package com.emazon.ms_stock.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CategoryReqDTO {
    private String name;
    private String description;
}
