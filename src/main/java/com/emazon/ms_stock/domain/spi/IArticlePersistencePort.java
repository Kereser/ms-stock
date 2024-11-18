package com.emazon.ms_stock.domain.spi;

import com.emazon.ms_stock.application.dto.handlers.PageDTO;
import com.emazon.ms_stock.application.dto.handlers.PageHandler;
import com.emazon.ms_stock.application.dto.out.ArticlesPriceDTO;
import com.emazon.ms_stock.domain.model.Article;

import java.util.List;
import java.util.Set;

public interface IArticlePersistencePort extends IBasicCrudPersistencePort<Article> {
    PageDTO<Article> findAll(PageHandler page);
    List<Article> findAllById(Iterable<Long> ids);
    void saveAll(Iterable<Article> articles);
    Set<ArticlesPriceDTO> getArticlesPrice(Set<Long> articleIds);
}
