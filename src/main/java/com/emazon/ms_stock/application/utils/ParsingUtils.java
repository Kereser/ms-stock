package com.emazon.ms_stock.application.utils;

import com.emazon.ms_stock.ConsUtils;
import com.emazon.ms_stock.application.dto.handlers.PageHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParsingUtils {
    private static final List<String> VALID_DIRECTIONS = List.of("asc", "desc");

    private ParsingUtils() {
    }

    public static PageHandler getHandlerFromParams(String direction, Integer pageSize, Integer page, String column) {
        return new PageHandler(ParsingUtils.getSortDirectionOrDefault(direction), pageSize, page, column);
    }

    public static PageHandler getHandlerFromParams(String direction, Integer pageSize, Integer page, String column, Map<String, Set<Long>> filters) {
        return new PageHandler(ParsingUtils.getSortDirectionOrDefault(direction), pageSize, page, column, filters);
    }

    public static Pageable toPageable(PageHandler page) {
        return PageRequest.of(page.getPage(), page.getPageSize(), getDirection(page.getDirection()), page.getColumn());
    }

    private static Sort.Direction getDirection(String direction) {
        return direction.equalsIgnoreCase(ConsUtils.SORT_ASC_VALUE) ? Sort.Direction.ASC : Sort.Direction.DESC;
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
