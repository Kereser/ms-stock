package com.emazon.ms_stock.domain.spi;

import com.emazon.ms_stock.application.dto.PageDTO;
import com.emazon.ms_stock.application.dto.PageHandler;
import com.emazon.ms_stock.domain.model.Article;

import java.util.Optional;

public interface IArticlePersistencePort extends IBasicCrudPersistencePort<Article> {
    Optional<Article> findByName(String name);
    PageDTO<Article> findAllByCategoryNameAsc(PageHandler page);
    PageDTO<Article> findAllByCategoryNameDesc(PageHandler page);
}
