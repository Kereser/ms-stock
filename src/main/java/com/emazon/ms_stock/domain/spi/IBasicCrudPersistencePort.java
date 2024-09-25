package com.emazon.ms_stock.domain.spi;

public interface IBasicCrudPersistencePort<T> {
    void save(T entity);

}
