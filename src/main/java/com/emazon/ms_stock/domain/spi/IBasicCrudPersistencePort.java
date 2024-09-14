package com.emazon.ms_stock.domain.spi;

import com.emazon.ms_stock.application.dto.PageDTO;
import com.emazon.ms_stock.application.dto.PageHandler;

public interface IBasicCrudPersistencePort<T> {
    void save(T entity);
    PageDTO<T> findAllPageable(PageHandler page);
}
