package com.emazon.ms_stock.domain.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBasicCrudServicePort<T> {
    void save(T entity);
    void update(T entity);
    void delete(Long id);
    Page<T> findAllPageable(Pageable pageable);
}
