package com.emazon.ms_stock.domain.spi;

import com.emazon.ms_stock.domain.model.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryPersistencePort extends IBasicCrudPersistencePort<Category> {
    Optional<Category> findByName(String name);
    List<Category> findAllById(Iterable<Long> ids);
}
