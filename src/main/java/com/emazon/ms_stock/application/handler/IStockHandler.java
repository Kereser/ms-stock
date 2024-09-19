package com.emazon.ms_stock.application.handler;

import com.emazon.ms_stock.application.dto.*;

import java.util.Set;

public interface IStockHandler {
    void saveCategoryInStock(CategoryReqDTO reqDTO);
    PageDTO<CategoryResDTO> getAllCategories(String order, Integer pageSize, Integer page, String column);

    void createBrandInStock(BrandReqDTO dto);
    PageDTO<BrandResDTO> getAllBrands(String direction, Integer pageSize, Integer page, String column);

    void createArticleInStock(ArticleReqDTO dto);

    PageDTO<ArticleResDTO> getAllArticles(String direction, Integer pageSize, Integer page, String column);

    void addSupply(Set<ItemQuantityDTO> dto);

    void handleCartAdditionValidations(Set<ItemQuantityDTO> dto);
}
