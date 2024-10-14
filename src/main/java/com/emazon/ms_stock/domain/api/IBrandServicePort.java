package com.emazon.ms_stock.domain.api;

import com.emazon.ms_stock.domain.model.Brand;

import java.util.List;

public interface IBrandServicePort extends IBasicCrudServicePort<Brand> {
    List<Brand> findAllByNames(List<String> names);
}
