package com.emazon.ms_stock.domain.model.sort;

import com.emazon.ms_stock.ConsUtils;
import com.emazon.ms_stock.application.dto.handlers.PageDTO;
import com.emazon.ms_stock.application.dto.handlers.PageHandler;
import com.emazon.ms_stock.application.utils.ParsingUtils;
import com.emazon.ms_stock.domain.model.Article;
import com.emazon.ms_stock.domain.spi.IArticlePersistencePort;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public class CategoryBrandNameStrategy implements SortingStrategy {

    private final IArticlePersistencePort articlePersistencePort;

    public CategoryBrandNameStrategy(IArticlePersistencePort articlePersistencePort) {
        this.articlePersistencePort = articlePersistencePort;
    }

    @Override
    public PageDTO<Article> sort(PageHandler pageHandler) {
        Pageable pageable = ParsingUtils.toPageableUnsorted(pageHandler);
        Set<Long> articleIds = pageHandler.getFilters().getOrDefault(ConsUtils.ARTICLE_IDS, null);

        if (isAscending(pageHandler.getDirection())) {
            return articlePersistencePort.findAllByArticleIdsAndCategoryAndBrandNameAsc(pageable, articleIds);
        }

        return articlePersistencePort.findAllByArticleIdsCategoryAndBrandNameDesc(pageable, articleIds);
    }
}
