package com.emazon.ms_stock.application.handler;

import com.emazon.ms_stock.application.dto.BrandReqDTO;
import com.emazon.ms_stock.application.dto.CategoryReqDTO;
import com.emazon.ms_stock.application.dto.CategoryResDTO;
import com.emazon.ms_stock.application.dto.PageDTO;

public interface IStockHandler {
    void saveCategoryInStock(CategoryReqDTO reqDTO);
    PageDTO<CategoryResDTO> getAllCategories(String order, Integer pageSize, Integer page);

    void createBrandInStock(BrandReqDTO dto);
}
