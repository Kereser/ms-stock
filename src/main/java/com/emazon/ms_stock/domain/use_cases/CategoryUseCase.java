package com.emazon.ms_stock.domain.use_cases;


import com.emazon.ms_stock.domain.api.ICategoryServicePort;
import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.domain.spi.ICategoryPersistencePort;
import com.emazon.ms_stock.infra.exception.CategoryAlreadyExists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class CategoryUseCase implements ICategoryServicePort {

    private final ICategoryPersistencePort persistencePort;

    public CategoryUseCase(ICategoryPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public void save(Category category) {
        Optional<Category> optCategory = persistencePort.findByName(category.getName());

        if (optCategory.isPresent()) {
            throw new CategoryAlreadyExists();
        }

        persistencePort.save(category);
    }

    @Override
    public void update(Category category) {
        persistencePort.update(category);
    }

    @Override
    public void delete(Long id) {
        persistencePort.delete(id);
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return persistencePort.findAll(pageable);
    }

    @Override
    public Category findById(Long id) {
        return persistencePort.findById(id);
    }
}
