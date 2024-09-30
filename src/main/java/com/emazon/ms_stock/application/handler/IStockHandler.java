package com.emazon.ms_stock.application.handler;

import com.emazon.ms_stock.application.dto.*;
import com.emazon.ms_stock.application.dto.handlers.PageDTO;
import com.emazon.ms_stock.application.dto.input.ArticleReqDTO;
import com.emazon.ms_stock.application.dto.input.BrandReqDTO;
import com.emazon.ms_stock.application.dto.out.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;

public interface IStockHandler {
    PageDTO<CategoryResDTO> getAllCategories(String order, Integer pageSize, Integer page, String column);
    PageDTO<BrandResDTO> getAllBrands(String direction, Integer pageSize, Integer page, String column);
    PageDTO<ArticleResDTO> getAllArticles(String direction, Integer pageSize, Integer page, String column);

    void saveCategoryInStock(CategoryReqDTO reqDTO);
    void createArticleInStock(ArticleReqDTO dto);
    void createBrandInStock(BrandReqDTO dto);

    void addSupply(Set<ItemQuantityDTO> dto);

    void validationsOnStockForCart(Set<ItemQuantityDTO> dto);

    PageDTO<ArticleResDTO> getArticlesForCart(String direction, Integer pageSize, Integer page, String columns, Set<Long> articleIds);
    Set<ArticlesPriceDTO> getArticlesPrice(Set<Long> articleIds);

    void processStockReduction(@Valid ItemsReqDTO itemsReqDTO);
    List<ArticleResDTO> getAllArticles(ItemsReqDTO itemsReqDTO);

    void processRollback(@Valid ItemsReqDTO itemsReqDTO);
}
