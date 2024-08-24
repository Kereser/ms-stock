package com.emazon.ms_stock.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PageDTO<T> {
    private int totalElements;
    private int totalPages;
    private Pageable pageable;
    private List<T> content;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Pageable {
        private int pageNumber;
        private int pageSize;
    }
}
