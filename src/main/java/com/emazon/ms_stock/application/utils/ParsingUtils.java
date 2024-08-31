package com.emazon.ms_stock.application.utils;

import com.emazon.ms_stock.application.dto.PageHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public class ParsingUtils {
    private static final List<String> VALID_DIRECTIONS = List.of("asc", "desc");

    private ParsingUtils() {
    }

    public static Pageable toPageable(PageHandler page) {
        Sort.Direction dir = page.getDirection().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        return PageRequest.of(page.getPage(), page.getPageSize(), Sort.by(dir, page.getColumn()));
    }

    public static Pageable toPageableUnsorted(PageHandler page) {
        return PageRequest.of(page.getPage(), page.getPageSize());
    }

    public static String getSortDirectionOrDefault(String direction) {
        if (VALID_DIRECTIONS.contains(direction.toLowerCase())) {
            return direction.toUpperCase();
        }
        return VALID_DIRECTIONS.get(0).toUpperCase();
    }
}
