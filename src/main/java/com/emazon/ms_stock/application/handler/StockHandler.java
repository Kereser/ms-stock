package com.emazon.ms_stock.application.handler;

import com.emazon.ms_stock.application.dto.*;
import com.emazon.ms_stock.domain.api.IArticleServicePort;
import com.emazon.ms_stock.domain.api.IBrandServicePort;
import com.emazon.ms_stock.domain.api.ICategoryServicePort;
import com.emazon.ms_stock.domain.model.Brand;
import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.infra.exception.InvalidRequestParam;
import com.emazon.ms_stock.infra.out.jpa.mapper.ArticleEntityMapper;
import com.emazon.ms_stock.infra.out.jpa.mapper.BrandEntityMapper;
import com.emazon.ms_stock.infra.out.jpa.mapper.CategoryEntityMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StockHandler implements IStockHandler {

    private final ICategoryServicePort categoryServicePort;
    private final CategoryEntityMapper categoryEntityMapper;

    private final IBrandServicePort brandServicePort;
    private final BrandEntityMapper brandEntityMapper;

    private final IArticleServicePort articleServicePort;
    private final ArticleEntityMapper articleEntityMapper;

    @Override
    public void saveCategoryInStock(CategoryReqDTO reqDTO) {
        Category category = new Category(reqDTO.getName(), reqDTO.getDescription());

        categoryServicePort.save(category);
    }

    @Override
    public PageDTO<CategoryResDTO> getAllCategories(String direction, Integer pageSize, Integer page, String column) {
        try {
            Category.SortBy.valueOf(column.toUpperCase());
            Sort.Direction.valueOf(direction.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestParam("Invalid request param. Please review the api documentation to check for valid values");
        }
        PageDTO<Category> categoryPages = categoryServicePort.findAllPageable(new PageHandler(pageSize, page, direction, column));
        return categoryEntityMapper.toCategoryRes(categoryPages);
    }

    @Override
    public void createBrandInStock(BrandReqDTO dto) {
        Brand brand = brandEntityMapper.toBrand(dto);
        brandServicePort.save(brand);
    }

    @Override
    public PageDTO<BrandResDTO> getAllBrands(String direction, Integer pageSize, Integer page, String column) {
        try {
            Brand.SortBy.valueOf(column.toUpperCase());
            Sort.Direction.valueOf(direction.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestParam("Invalid request param. Please review the api documentation to check for valid values");
        }
        PageHandler pageHandler = new PageHandler(pageSize, page, direction, column);
        PageDTO<Brand> brandPagesDTO = brandServicePort.findAllPageable(pageHandler);
        return brandEntityMapper.toBrandResPage(brandPagesDTO);
    }

    @Override
    public void createArticleInStock(ArticleReqDTO dto) {
        articleServicePort.save(articleEntityMapper.toArticle(dto));
    }
}
