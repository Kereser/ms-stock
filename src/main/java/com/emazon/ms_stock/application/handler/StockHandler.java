package com.emazon.ms_stock.application.handler;

import com.emazon.ms_stock.application.dto.CategoryReqDTO;
import com.emazon.ms_stock.domain.api.ICategoryServicePort;
import com.emazon.ms_stock.domain.model.Category;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StockHandler implements IStockHandler {

    private final ICategoryServicePort categoryServicePort;

    @Override
    public void saveCategoryInStock(CategoryReqDTO reqDTO) {
        Category category = new Category(reqDTO.getName(), reqDTO.getDescription());

        categoryServicePort.save(category);
    }
}
