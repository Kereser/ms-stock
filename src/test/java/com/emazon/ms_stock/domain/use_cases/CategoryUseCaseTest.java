package com.emazon.ms_stock.domain.use_cases;

import com.emazon.ms_stock.application.dto.PageHandler;
import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.domain.spi.ICategoryPersistencePort;
import com.emazon.ms_stock.infra.exception.CategoryAlreadyExists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CategoryUseCaseTest {

    @Mock
    private ICategoryPersistencePort iCategoryPersistencePort;

    @InjectMocks
    private CategoryUseCase categoryUseCase;

    private final Category category = new Category("Test cat", "Test desc");

    @Test
    @DisplayName(value = "Throws an exception when a category with the same name is already saved.")
    void Should_throwsAnException_When_CategoryAlreadyExists() {
        Mockito.when(iCategoryPersistencePort.findByName(Mockito.anyString())).thenReturn(Optional.of(category));

        assertThrows(CategoryAlreadyExists.class, () -> categoryUseCase.save(category));
    }

    @Test
    @DisplayName(value = "Test valid insertion of category.")
    void Should_SaveCategory_When_UniqueName() {
        Mockito.when(iCategoryPersistencePort.findByName(Mockito.anyString())).thenReturn(Optional.empty());

        Category cat = new Category("Not same name", "daffsaf");
        categoryUseCase.save(cat);

        Mockito.verify(iCategoryPersistencePort, Mockito.times(1)).findByName(cat.getName());
    }

    @Test
    @DisplayName(value = "Test valid calls to persistence port when getting all categories.")
    void Should_InteractWithPersistencePortOneTimeOnly_When_ValidPageRequest() {
        categoryUseCase.findAllPageable(PageHandler.builder().pageSize(20).page(0).build());

        Mockito.verify(iCategoryPersistencePort, Mockito.times(1)).findAllPageable(Mockito.any());
    }
}