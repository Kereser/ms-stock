package com.emazon.ms_stock.application.dto.handlers;

import lombok.*;

import java.util.Map;

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
    private Map<String, String> filters;

    public PageHandler(String direction, Integer pageSize, Integer page, String column) {
        this.direction = direction;
        this.pageSize = pageSize;
        this.page = page;
        this.column = column;
        this.filters = Map.of();
    }
}
