package com.emazon.ms_stock.domain.model.sort;

import com.emazon.ms_stock.ConsUtils;
import com.emazon.ms_stock.application.dto.handlers.PageDTO;
import com.emazon.ms_stock.application.dto.handlers.PageHandler;
import com.emazon.ms_stock.domain.model.Article;

import java.util.Set;

public interface SortingStrategy {
    PageDTO<Article> sort(PageHandler pageHandler);
    default boolean isAscending(String direction) {
        return direction.equalsIgnoreCase(ConsUtils.SORT_ASC_VALUE) ? Boolean.TRUE : Boolean.FALSE;
    }

    default Set<Long> getArticleIdsFromFilters(PageHandler pageHandler) {
        // This could be extended to get all articleIds of filters like brandId or categoryId.
        return pageHandler.getFilters().getOrDefault(ConsUtils.ARTICLE_IDS, null);
    }
}
