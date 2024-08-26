package com.emazon.ms_stock.domain.use_cases;

import com.emazon.ms_stock.domain.api.IBrandServicePort;
import com.emazon.ms_stock.domain.model.Brand;
import com.emazon.ms_stock.domain.spi.IBrandPersistencePort;
import com.emazon.ms_stock.infra.exception.BrandAlreadyExists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class BrandUseCase implements IBrandServicePort {

    private final IBrandPersistencePort persistencePort;

    public BrandUseCase(IBrandPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public void save(Brand brand) {
        Optional<Brand> opt = persistencePort.findByName(brand.getName());

        if (opt.isPresent()) throw new BrandAlreadyExists();

        persistencePort.save(brand);
    }

    @Override
    public void update(Brand brand) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Page<Brand> findAllPageable(Pageable pageable) {
        return null;
    }
}
