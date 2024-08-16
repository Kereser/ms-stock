package com.emazon.ms_stock.domain.api;

import com.emazon.ms_stock.domain.model.Category;

import java.util.List;

public interface ICategoryServicePort {

    void save(Category category);
    void update(Category category);
    void delete(Long id);
    List<Category> getAll();
    Category get(Long id);
}
