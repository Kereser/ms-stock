package com.emazon.ms_stock.application.handler;

import com.emazon.ms_stock.application.dto.CategoryReqDTO;

public interface IStockHandler {
    void saveCategoryInStock(CategoryReqDTO reqDTO);
}
