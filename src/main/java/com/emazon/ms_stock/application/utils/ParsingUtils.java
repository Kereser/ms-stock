package com.emazon.ms_stock.application.utils;

import com.emazon.ms_stock.application.dto.PageHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ParsingUtils {
    private ParsingUtils() {
    }

    public static Pageable toPageable(PageHandler page) {
        return PageRequest.of(page.getPage(), page.getPageSize(), Sort.Direction.valueOf(page.getDirection().toUpperCase()), page.getColumn());
    }
}
