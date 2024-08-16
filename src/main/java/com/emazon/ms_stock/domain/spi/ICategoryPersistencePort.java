package com.emazon.ms_stock.domain.spi;

import com.emazon.ms_stock.domain.model.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryPersistencePort {
    void save(Category category);
    void update(Category category);
    void delete(Long id);
    List<Category> getAll();
    Category get(Long id);
    Optional<Category> getByName(String name);
}
