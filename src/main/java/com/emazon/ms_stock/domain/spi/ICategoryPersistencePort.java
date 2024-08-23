package com.emazon.ms_stock.domain.spi;

import com.emazon.ms_stock.domain.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ICategoryPersistencePort {
    void save(Category category);
    void update(Category category);
    void delete(Long id);
    Page<Category> findAll(Pageable pageable);
    Category findById(Long id);
    Optional<Category> findByName(String name);
}
