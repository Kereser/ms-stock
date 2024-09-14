package com.emazon.ms_stock.domain.spi;

import com.emazon.ms_stock.application.dto.PageDTO;
import com.emazon.ms_stock.application.dto.PageHandler;
import com.emazon.ms_stock.domain.model.Article;

import java.util.List;

public interface IArticlePersistencePort extends IBasicCrudPersistencePort<Article> {
    PageDTO<Article> findAllByCategoryNameAsc(PageHandler page);
    PageDTO<Article> findAllByCategoryNameDesc(PageHandler page);
    List<Article> findAllById(Iterable<Long> ids);
    void saveAll(Iterable<Article> articles);
}
