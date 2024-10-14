package com.emazon.ms_stock.infra.out.jpa.adapter;

import com.emazon.ms_stock.application.dto.handlers.PageDTO;
import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.domain.spi.ICategoryPersistencePort;
import com.emazon.ms_stock.infra.out.jpa.entity.CategoryEntity;
import com.emazon.ms_stock.infra.out.jpa.mapper.CategoryEntityMapper;
import com.emazon.ms_stock.infra.out.jpa.repository.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CategoryJpaAdapter implements ICategoryPersistencePort {

    private final CategoryJpaRepository categoryJpaRepository;
    private final CategoryEntityMapper categoryEntityMapper;

    @Override
    public void save(Category category) {
        categoryJpaRepository.save(categoryEntityMapper.toEntity(category));
    }

    @Override
    public PageDTO<Category> findAllPageable(Pageable page) {
        PageDTO<CategoryEntity> entityPages = categoryEntityMapper.toEntityPage(categoryJpaRepository.findAll(page));
        return categoryEntityMapper.toCategoryPage(entityPages);
    }

    @Override
    public Optional<Category> findByName(String name) {
        Optional<CategoryEntity> optCategory = categoryJpaRepository.findByName(name);

        return optCategory.map(categoryEntityMapper::toCategory);
    }

    @Override
    public List<Category> findAllById(Iterable<Long> ids) {
        return categoryEntityMapper.toCategoryList(categoryJpaRepository.findAllById(ids));
    }

    @Override
    public List<Category> findAllByName(List<String> names) {
        return categoryEntityMapper.toCategoryList(categoryJpaRepository.findByNameIn(names));
    }

    public List<Category> findAll() {
        return categoryEntityMapper.toCategoryList(categoryJpaRepository.findAll());
    }
}
