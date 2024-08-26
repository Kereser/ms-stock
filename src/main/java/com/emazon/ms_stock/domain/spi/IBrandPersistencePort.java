package com.emazon.ms_stock.domain.spi;

import com.emazon.ms_stock.domain.model.Brand;

import java.util.Optional;

public interface IBrandPersistencePort extends IBasicCrudPersistencePort<Brand> {
    Optional<Brand> findByName(String name);
}
