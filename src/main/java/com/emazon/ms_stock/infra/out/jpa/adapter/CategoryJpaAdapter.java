package com.emazon.ms_stock.infra.out.jpa.adapter;

import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.domain.spi.ICategoryPersistencePort;
import com.emazon.ms_stock.infra.out.jpa.entity.CategoryEntity;
import com.emazon.ms_stock.infra.out.jpa.repository.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CategoryJpaAdapter implements ICategoryPersistencePort {

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public void save(Category category) {
        Optional<CategoryEntity> optCategory = categoryJpaRepository.findByName(category.getName());

        if (optCategory.isPresent()) {
            throw new DuplicateKeyException("Name must be unique");
        }

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(category.getName());
        categoryEntity.setDescription(category.getDescription());
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
        return Optional.empty();
    }
}
