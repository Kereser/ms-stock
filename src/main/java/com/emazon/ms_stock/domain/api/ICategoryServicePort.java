package com.emazon.ms_stock.domain.api;

import com.emazon.ms_stock.domain.model.Category;

import java.util.List;

public interface ICategoryServicePort extends IBasicCrudServicePort<Category> {
    List<Category> findCategoriesByName(List<String> names);
}
