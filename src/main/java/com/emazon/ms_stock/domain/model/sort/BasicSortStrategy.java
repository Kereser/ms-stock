package com.emazon.ms_stock.domain.model.sort;

import com.emazon.ms_stock.ConsUtils;
import com.emazon.ms_stock.application.dto.handlers.PageDTO;
import com.emazon.ms_stock.application.dto.handlers.PageHandler;
import com.emazon.ms_stock.application.utils.ParsingUtils;
import com.emazon.ms_stock.domain.model.Article;
import com.emazon.ms_stock.domain.spi.IArticlePersistencePort;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public class BasicSortStrategy implements SortingStrategy {

    private final IArticlePersistencePort articlePersistencePort;

    public BasicSortStrategy(IArticlePersistencePort articlePersistencePort) {
        this.articlePersistencePort = articlePersistencePort;
    }

    @Override
    public PageDTO<Article> sort(PageHandler pageHandler) {
        Pageable pageable = ParsingUtils.toPageable(pageHandler);
        Set<Long> articleIds = pageHandler.getFilters().getOrDefault(ConsUtils.ARTICLE_IDS, null);

        return articlePersistencePort.findAllByArticleIdPageable(pageable, articleIds);
    }
}
