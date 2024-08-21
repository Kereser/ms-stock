package com.emazon.ms_stock.domain.use_cases;


import com.emazon.ms_stock.domain.api.ICategoryServicePort;
import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.domain.spi.ICategoryPersistencePort;
import com.emazon.ms_stock.infra.exception.CategoryAlreadyExists;
import java.util.List;
import java.util.Optional;

public class CategoryUseCase implements ICategoryServicePort {

    private final ICategoryPersistencePort persistencePort;

    public CategoryUseCase(ICategoryPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public void save(Category category) {
        Optional<Category> optCategory = persistencePort.getByName(category.getName());

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
    public List<Category> getAll() {
        return persistencePort.getAll();
    }

    @Override
    public Category get(Long id) {
        return persistencePort.get(id);
    }
}
