package com.emazon.ms_stock.infra.out.jpa.adapter;

import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.domain.spi.ICategoryPersistencePort;
import com.emazon.ms_stock.infra.out.jpa.entity.CategoryEntity;
import com.emazon.ms_stock.infra.out.jpa.repository.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CategoryJpaAdapter implements ICategoryPersistencePort {

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public void save(Category category) {
        CategoryEntity categoryEntity = CategoryEntity.builder().name(category.getName()).description(category.getDescription()).build();

        categoryJpaRepository.save(categoryEntity);
    }

    @Override
    public void update(Category category) {
        CategoryEntity categoryEntity = new CategoryEntity();

        categoryEntity.setName(category.getName());
        categoryEntity.setDescription(category.getDescription());

        categoryJpaRepository.save(categoryEntity);
    }

    @Override
    public void delete(Long id) {
        categoryJpaRepository.deleteById(id);
    }

    @Override
    public List<Category> getAll() {
        return List.of();
    }

    @Override
    public Category get(Long id) {
        return null;
    }

    @Override
    public Optional<Category> getByName(String name) {
        Optional<CategoryEntity> optCategory = categoryJpaRepository.findByName(name);

        if (optCategory.isEmpty()) {
            return Optional.empty();
        }

        CategoryEntity entity = optCategory.get();
        Category category = new Category(entity.getName(), entity.getDescription());
        return Optional.of(category);
    }
}
