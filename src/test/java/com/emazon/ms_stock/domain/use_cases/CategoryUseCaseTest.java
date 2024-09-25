package com.emazon.ms_stock.domain.use_cases;

import com.emazon.ms_stock.ConsUtils;
import com.emazon.ms_stock.application.dto.handlers.PageHandler;
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
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CategoryUseCaseTest {

    @Mock
    private ICategoryPersistencePort iCategoryPersistencePort;

    @InjectMocks
    private CategoryUseCase categoryUseCase;

    private final Category category = new Category(ConsUtils.TEST_NAME, ConsUtils.VALID_DESC);

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

        Category cat = new Category(ConsUtils.TEST_NAME, ConsUtils.VALID_DESC);
        categoryUseCase.save(cat);

        Mockito.verify(iCategoryPersistencePort, Mockito.times(ConsUtils.INTEGER_1)).findByName(cat.getName());
        Mockito.verify(iCategoryPersistencePort, Mockito.times(ConsUtils.INTEGER_1)).save(Mockito.any());
    }

    @Test
    @DisplayName(value = "Test valid calls to persistence port when getting all categories.")
    void Should_InteractWithPersistencePortOneTimeOnly_When_ValidPageRequest() {
        categoryUseCase.findAllPageable(PageHandler.builder().page(ConsUtils.INTEGER_0)
                .pageSize(ConsUtils.INTEGER_20)
                .direction(Sort.Direction.ASC.name())
                .column(ConsUtils.NAME_PARAM_VALUE)
                .build());

        Mockito.verify(iCategoryPersistencePort, Mockito.times(ConsUtils.INTEGER_1)).findAllPageable(Mockito.any());
    }
}