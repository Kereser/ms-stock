package com.emazon.ms_stock.application.handler;

import com.emazon.ms_stock.application.dto.CategoryReqDTO;
import com.emazon.ms_stock.application.dto.CategoryResDTO;
import org.springframework.data.domain.Page;

public interface IStockHandler {
    void saveCategoryInStock(CategoryReqDTO reqDTO);
    Page<CategoryResDTO> getAllCategories(String order, Integer pageSize, Integer page);
}
