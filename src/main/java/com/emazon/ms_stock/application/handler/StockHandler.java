package com.emazon.ms_stock.application.handler;

import com.emazon.ms_stock.application.mapper.ArticleDTOMapper;
import com.emazon.ms_stock.application.dto.*;
import com.emazon.ms_stock.application.mapper.BrandDTOMapper;
import com.emazon.ms_stock.application.mapper.CategoryDTOMapper;
import com.emazon.ms_stock.application.utils.ParsingUtils;
import com.emazon.ms_stock.domain.api.IArticleServicePort;
import com.emazon.ms_stock.domain.api.IBrandServicePort;
import com.emazon.ms_stock.domain.api.ICategoryServicePort;
import com.emazon.ms_stock.domain.model.Article;
import com.emazon.ms_stock.domain.model.Brand;
import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.infra.exception.InvalidRequestParam;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StockHandler implements IStockHandler {

    private final ICategoryServicePort categoryServicePort;
    private final CategoryDTOMapper categoryDTOMapper;

    private final IBrandServicePort brandServicePort;
    private final BrandDTOMapper brandDTOMapper;

    private final IArticleServicePort articleServicePort;
    private final ArticleDTOMapper articleDTOMapper;

    private static final String INVALID_SORT_CRITERIA = "Invalid sort criteria in request param";

    @Override
    public void saveCategoryInStock(CategoryReqDTO reqDTO) {
        Category category = categoryDTOMapper.toCategory(reqDTO);
        categoryServicePort.save(category);
    }

    @Override
    public PageDTO<CategoryResDTO> getAllCategories(String direction, Integer pageSize, Integer page, String column) {
        if (Boolean.FALSE.equals(Category.isValidSortField(column))) {
            throw new InvalidRequestParam(INVALID_SORT_CRITERIA);
        }
        PageHandler pageHandler = new PageHandler(ParsingUtils.getSortDirectionOrDefault(direction), pageSize, page, column);
        PageDTO<Category> categoryPages = categoryServicePort.findAllPageable(pageHandler);
        return categoryDTOMapper.toPageResDTO(categoryPages);
    }

    @Override
    public void createBrandInStock(BrandReqDTO dto) {
        Brand brand = brandDTOMapper.toBrand(dto);
        brandServicePort.save(brand);
    }

    @Override
    public PageDTO<BrandResDTO> getAllBrands(String direction, Integer pageSize, Integer page, String column) {
        if (Boolean.FALSE.equals(Brand.isValidSortField(column))) {
            throw new InvalidRequestParam(INVALID_SORT_CRITERIA);
        }
        PageHandler pageHandler = new PageHandler(ParsingUtils.getSortDirectionOrDefault(direction), pageSize, page, column);
        PageDTO<Brand> brandPagesDTO = brandServicePort.findAllPageable(pageHandler);
        return brandDTOMapper.toPageResDTO(brandPagesDTO);
    }

    @Override
    public void createArticleInStock(ArticleReqDTO dto) {
        articleServicePort.save(articleDTOMapper.toArticle(dto));
    }

    @Override
    public PageDTO<ArticleResDTO> getAllArticles(String direction, Integer pageSize, Integer page, String column) {
        if (Boolean.FALSE.equals(Article.isValidSortField(column))) {
            throw new InvalidRequestParam(INVALID_SORT_CRITERIA);
        }
        PageHandler pageHandler = new PageHandler(ParsingUtils.getSortDirectionOrDefault(direction), pageSize, page, column);
        PageDTO<Article> articlePageDTO = articleServicePort.findAllPageable(pageHandler);
        return articleDTOMapper.toPageResDTO(articlePageDTO);
    }
}
