package com.emazon.ms_stock.domain.api;

import com.emazon.ms_stock.domain.model.Category;

import java.util.Optional;

public interface ICategoryServicePort extends IBasicCrudServicePort<Category> {
    Optional<Category> findById(Long id);
}
