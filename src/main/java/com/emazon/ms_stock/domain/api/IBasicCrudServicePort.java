package com.emazon.ms_stock.domain.api;

import com.emazon.ms_stock.application.dto.PageDTO;
import com.emazon.ms_stock.application.dto.PageHandler;

public interface IBasicCrudServicePort<T> {
    void save(T entity);
    void update(T entity);
    void delete(Long id);
    PageDTO<T> findAllPageable(PageHandler pageable);
}
