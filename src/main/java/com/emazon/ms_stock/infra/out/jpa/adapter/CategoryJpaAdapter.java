package com.emazon.ms_stock.infra.out.jpa.adapter;

import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.domain.spi.ICategoryPersistencePort;
import com.emazon.ms_stock.infra.out.jpa.entity.CategoryEntity;
import com.emazon.ms_stock.infra.out.jpa.mapper.CategoryEntityMapper;
import com.emazon.ms_stock.infra.out.jpa.repository.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    public void update(Category category) {
        CategoryEntity categoryEntity = categoryEntityMapper.toEntity(category);

        categoryJpaRepository.save(categoryEntity);
    }

    @Override
    public void delete(Long id) {
        categoryJpaRepository.deleteById(id);
    }

    @Override
    public Page<Category> findAllPageable(Pageable pageable) {
        return categoryEntityMapper.toCategoryPage(categoryJpaRepository.findAll(pageable));
    }

    @Override
    public Category findById(Long id) {
        return null;
    }

    @Override
    public Optional<Category> findByName(String name) {
        Optional<CategoryEntity> optCategory = categoryJpaRepository.findByName(name);

        return optCategory.map(categoryEntityMapper::toCategory);
    }
}
