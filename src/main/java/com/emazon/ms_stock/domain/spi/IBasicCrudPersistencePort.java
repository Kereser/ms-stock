package com.emazon.ms_stock.domain.spi;

import com.emazon.ms_stock.application.dto.PageDTO;
import com.emazon.ms_stock.application.dto.PageHandler;

import java.util.Optional;

public interface IBasicCrudPersistencePort<T> {
    void save(T entity);
    void update(T entity);
    void delete(Long id);
    PageDTO<T> findAllPageable(PageHandler page);
    Optional<T> findById(Long id);
}
