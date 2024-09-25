package com.emazon.ms_stock.domain.spi;

import com.emazon.ms_stock.application.dto.handlers.PageDTO;
import com.emazon.ms_stock.domain.model.Category;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ICategoryPersistencePort extends IBasicCrudPersistencePort<Category> {
    Optional<Category> findByName(String name);
    List<Category> findAllById(Iterable<Long> ids);
    PageDTO<Category> findAllPageable(Pageable page);
}
