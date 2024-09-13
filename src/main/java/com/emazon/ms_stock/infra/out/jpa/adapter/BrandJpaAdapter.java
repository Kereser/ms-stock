package com.emazon.ms_stock.infra.out.jpa.adapter;

import com.emazon.ms_stock.application.dto.PageDTO;
import com.emazon.ms_stock.application.dto.PageHandler;
import com.emazon.ms_stock.application.utils.ParsingUtils;
import com.emazon.ms_stock.domain.model.Brand;
import com.emazon.ms_stock.domain.spi.IBrandPersistencePort;
import com.emazon.ms_stock.infra.out.jpa.entity.BrandEntity;
import com.emazon.ms_stock.infra.out.jpa.mapper.BrandEntityMapper;
import com.emazon.ms_stock.infra.out.jpa.repository.BrandJpaRepository;
import lombok.RequiredArgsConstructor;

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
    public PageDTO<Brand> findAllPageable(PageHandler page) {
        PageDTO<BrandEntity> brandPages = brandEntityMapper.toEntityPage(brandJpaRepository.findAll(ParsingUtils.toPageable(page)));

        return brandEntityMapper.toBrandPage(brandPages);
    }

    @Override
    public Optional<Brand> findById(Long id) {
        Optional<BrandEntity> opt = brandJpaRepository.findById(id);

        return opt.map(brandEntityMapper::toBrand);
    }

    @Override
    public Optional<Brand> findByName(String name) {
        Optional<BrandEntity> opt = brandJpaRepository.findByName(name);

        return opt.map(brandEntityMapper::toBrand);
    }
}
