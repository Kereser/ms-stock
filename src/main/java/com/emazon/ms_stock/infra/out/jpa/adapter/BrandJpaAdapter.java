package com.emazon.ms_stock.infra.out.jpa.adapter;

import com.emazon.ms_stock.domain.model.Brand;
import com.emazon.ms_stock.domain.spi.IBrandPersistencePort;
import com.emazon.ms_stock.infra.out.jpa.entity.BrandEntity;
import com.emazon.ms_stock.infra.out.jpa.mapper.BrandEntityMapper;
import com.emazon.ms_stock.infra.out.jpa.repository.BrandJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@RequiredArgsConstructor
public class BrandJpaAdapter implements IBrandPersistencePort {

    private final BrandJpaRepository brandJpaRepository;
    private final BrandEntityMapper brandEntityMapper;

    @Override
    public void save(Brand brand) {
        brandJpaRepository.save(brandEntityMapper.toEntity(brand));
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

    @Override
    public Brand findById(Long id) {
        Optional<BrandEntity> opt = brandJpaRepository.findById(id);

        if (opt.isEmpty()) throw new EntityNotFoundException();

        return brandEntityMapper.toBrand(opt.get());
    }

    @Override
    public Optional<Brand> findByName(String name) {
        Optional<BrandEntity> opt = brandJpaRepository.findByName(name);

        return opt.map(brandEntityMapper::toBrand);
    }
}
