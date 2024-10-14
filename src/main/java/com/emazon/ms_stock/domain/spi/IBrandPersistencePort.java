package com.emazon.ms_stock.domain.spi;

import com.emazon.ms_stock.application.dto.handlers.PageDTO;
import com.emazon.ms_stock.domain.model.Brand;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IBrandPersistencePort extends IBasicCrudPersistencePort<Brand> {
    Optional<Brand> findByName(String name);
    Optional<Brand> findById(Long id);
    PageDTO<Brand> findAllPageable(Pageable page);

    List<Brand> findAllByNames(List<String> names);
}
