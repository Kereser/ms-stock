package com.emazon.ms_stock.domain.spi;

import com.emazon.ms_stock.domain.model.Article;

import java.util.Optional;

public interface IArticlePersistencePort extends IBasicCrudPersistencePort<Article> {
    Optional<Article> findByName(String name);
}
