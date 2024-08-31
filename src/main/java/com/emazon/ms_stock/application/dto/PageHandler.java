package com.emazon.ms_stock.application.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageHandler {
    private String direction;
    private Integer pageSize;
    private Integer page;
    private String column;
}
