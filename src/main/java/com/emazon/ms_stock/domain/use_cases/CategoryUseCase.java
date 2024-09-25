package com.emazon.ms_stock.domain.use_cases;


import com.emazon.ms_stock.application.dto.handlers.PageDTO;
import com.emazon.ms_stock.application.dto.handlers.PageHandler;
import com.emazon.ms_stock.application.utils.ParsingUtils;
import com.emazon.ms_stock.domain.api.ICategoryServicePort;
import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.domain.spi.ICategoryPersistencePort;
import com.emazon.ms_stock.infra.exception.CategoryAlreadyExists;

import java.util.Optional;

public class CategoryUseCase implements ICategoryServicePort {

    private final ICategoryPersistencePort persistencePort;

    public CategoryUseCase(ICategoryPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public void save(Category category) {
        validateCategory(category);

        persistencePort.save(category);
    }

    private void validateCategory(Category category) {
        Optional<Category> optCategory = persistencePort.findByName(category.getName());

        if (optCategory.isPresent()) throw new CategoryAlreadyExists();
    }

    @Override
    public PageDTO<Category> findAllPageable(PageHandler page) {
        return persistencePort.findAllPageable(ParsingUtils.toPageable(page));
    }
}
