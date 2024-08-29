package com.emazon.ms_stock.application.handler;

import com.emazon.ms_stock.application.dto.*;

public interface IStockHandler {
    void saveCategoryInStock(CategoryReqDTO reqDTO);
    PageDTO<CategoryResDTO> getAllCategories(String order, Integer pageSize, Integer page, String column);

    void createBrandInStock(BrandReqDTO dto);
    PageDTO<BrandResDTO> getAllBrands(String direction, Integer pageSize, Integer page, String column);

    void createArticleInStock(ArticleReqDTO dto);
}
