package com.emazon.ms_stock.domain.spi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBasicCrudPersistencePort<T> {
    void save(T brand);
    void update(T brand);
    void delete(Long id);
    Page<T> findAllPageable(Pageable pageable);
    T findById(Long id);
}
