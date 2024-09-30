package com.emazon.ms_stock.application.dto.handlers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PageDTO<T> {
    private Integer totalElements;
    private Integer totalPages;
    private Pageable pageable;
    private Integer numberOfElements;
    private Integer currentPage;
    private Integer size;
    private Boolean first;
    private Boolean last;
    private List<T> content;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pageable {
        private Integer pageNumber;
        private Integer pageSize;
        private Integer offset;
    }
}
