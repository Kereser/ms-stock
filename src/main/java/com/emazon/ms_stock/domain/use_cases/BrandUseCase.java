package com.emazon.ms_stock.domain.use_cases;

import com.emazon.ms_stock.application.dto.handlers.PageDTO;
import com.emazon.ms_stock.application.dto.handlers.PageHandler;
import com.emazon.ms_stock.application.utils.ParsingUtils;
import com.emazon.ms_stock.domain.api.IBrandServicePort;
import com.emazon.ms_stock.domain.model.Brand;
import com.emazon.ms_stock.domain.spi.IBrandPersistencePort;
import com.emazon.ms_stock.infra.exception.BrandAlreadyExists;

import java.util.List;
import java.util.Optional;

public class BrandUseCase implements IBrandServicePort {

    private final IBrandPersistencePort persistencePort;

    public BrandUseCase(IBrandPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public void save(Brand brand) {
        validateBrand(brand);

        persistencePort.save(brand);
    }

    private void validateBrand(Brand brand) {
        Optional<Brand> opt = persistencePort.findByName(brand.getName());

        if (opt.isPresent()) throw new BrandAlreadyExists();
    }

    @Override
    public PageDTO<Brand> findAllPageable(PageHandler page) {
        return persistencePort.findAllPageable(ParsingUtils.toPageable(page));
    }

    @Override
    public List<Brand> findAllByNames(List<String> names) {
        return persistencePort.findAllByNames(names);
    }
}
