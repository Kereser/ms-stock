package com.emazon.ms_stock.domain.use_cases;


import com.emazon.ms_stock.domain.api.ICategoryServicePort;
import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.domain.spi.ICategoryPersistencePort;

import java.util.List;

public class CategoryUseCase implements ICategoryServicePort {

    private final ICategoryPersistencePort persistencePort;

    public CategoryUseCase(ICategoryPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public void save(Category category) {

        if (category.getDescription().isEmpty() || category.getDescription().isBlank()) {
            throw new RuntimeException("Description cannot be empty");
        }

        String name = category.getName();
        if (name.isEmpty() || name.isBlank() || name.length() > 50) {
            throw new RuntimeException("Name must not be longer than 50 characters.");
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
    public List<Category> getAll() {
        return persistencePort.getAll();
    }

    @Override
    public Category get(Long id) {
        return persistencePort.get(id);
    }
}
