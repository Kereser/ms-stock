package com.emazon.ms_stock.infra.out.jpa.adapter;

import com.emazon.ms_stock.application.dto.PageDTO;
import com.emazon.ms_stock.application.dto.PageHandler;
import com.emazon.ms_stock.application.utils.ParsingUtils;
import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.domain.spi.ICategoryPersistencePort;
import com.emazon.ms_stock.infra.out.jpa.entity.CategoryEntity;
import com.emazon.ms_stock.infra.out.jpa.mapper.CategoryEntityMapper;
import com.emazon.ms_stock.infra.out.jpa.repository.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;

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
    public void update(Category category) {
        CategoryEntity categoryEntity = categoryEntityMapper.toEntity(category);

        categoryJpaRepository.save(categoryEntity);
    }

    @Override
    public void delete(Long id) {
        categoryJpaRepository.deleteById(id);
    }

    @Override
    public PageDTO<Category> findAllPageable(PageHandler page) {
        PageDTO<CategoryEntity> entityPages = categoryEntityMapper.toEntityPage(categoryJpaRepository.findAll(ParsingUtils.toPageable(page)));
        return categoryEntityMapper.toCategoryPage(entityPages);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return Optional.empty();
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

    public List<Category> findAll() {
        return categoryEntityMapper.toCategoryList(categoryJpaRepository.findAll());
    }
}
