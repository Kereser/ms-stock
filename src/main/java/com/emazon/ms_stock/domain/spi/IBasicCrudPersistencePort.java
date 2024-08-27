package com.emazon.ms_stock.domain.spi;

import com.emazon.ms_stock.application.dto.PageDTO;
import com.emazon.ms_stock.application.dto.PageHandler;

public interface IBasicCrudPersistencePort<T> {
    void save(T brand);
    void update(T brand);
    void delete(Long id);
    PageDTO<T> findAllPageable(PageHandler page);
    T findById(Long id);
}
