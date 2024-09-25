package com.emazon.ms_stock.application.handler;

import com.emazon.ms_stock.ConsUtils;
import com.emazon.ms_stock.application.dto.handlers.PageDTO;
import com.emazon.ms_stock.application.dto.handlers.PageHandler;
import com.emazon.ms_stock.application.dto.input.ArticleReqDTO;
import com.emazon.ms_stock.application.dto.input.BrandReqDTO;
import com.emazon.ms_stock.application.dto.out.*;
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

import java.util.List;
import java.util.Map;
import java.util.Set;

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

        PageDTO<Category> categoryPages = categoryServicePort.findAllPageable(buildPageHandler(direction, pageSize, page, column));
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

        PageDTO<Brand> brandPagesDTO = brandServicePort.findAllPageable(buildPageHandler(direction, pageSize, page, column));
        return brandDTOMapper.toPageResDTO(brandPagesDTO);
    }

    @Override
    public void createArticleInStock(ArticleReqDTO dto) {
        articleServicePort.save(articleDTOMapper.articleResDTOToArticle(dto));
    }

    @Override
    public PageDTO<ArticleResDTO> getAllArticles(String direction, Integer pageSize, Integer page, String column) {
        if (Boolean.FALSE.equals(Article.isValidSortField(column))) {
            throw new InvalidRequestParam(INVALID_SORT_CRITERIA);
        }

        PageDTO<Article> articlePageDTO = articleServicePort.findAllPageable(buildPageHandler(direction, pageSize, page, column));
        return articleDTOMapper.articlePageToArticleResPage(articlePageDTO);
    }

    @Override
    public void addSupply(Set<ItemQuantityDTO> itemQuantityDTOS) {
        articleServicePort.addSupply(itemQuantityDTOS);
    }

    @Override
    public void handleCartAdditionValidations(Set<ItemQuantityDTO> itemQuantityDTOS) {
        articleServicePort.handleCartAdditionValidations(itemQuantityDTOS);
    }

    @Override
    public PageDTO<ArticleResDTO> getArticlesForCart(String direction, Integer pageSize, Integer page, String columns, Set<Long> articleIds) {
        validColumnsToSort(columns);

        PageDTO<Article> articles = articleServicePort.findAllPageable(
                buildPageHandlerWithFilters(direction, pageSize, page, columns, Map.of(ConsUtils.ARTICLE_IDS, articleIds)));

        return articleDTOMapper.articlePageToArticleResPage(articles);
    }

    @Override
    public Set<ArticlesPriceDTO> getArticlesPrice(Set<Long> articleIds) {
        return articleServicePort.getArticlesPrice(articleIds);
    }

    private PageHandler buildPageHandlerWithFilters(String direction, Integer pageSize, Integer page, String columns, Map<String, Set<Long>> filters) {
        return ParsingUtils.getHandlerFromParams(direction, pageSize, page, columns, filters);
    }

    private PageHandler buildPageHandler(String direction, Integer pageSize, Integer page, String column) {
        return ParsingUtils.getHandlerFromParams(direction, pageSize, page, column);
    }

    private void validColumnsToSort(String columns) {
        String[] columnsArray = columns.split(",");

        if (columnsArray.length > 2) {
            throw new InvalidRequestParam(INVALID_SORT_CRITERIA);
        }

        if (columnsArray.length == 1) {
            validateSingleParam(columnsArray[0]);
        } else if (columnsArray.length == 2) {
            validateCombinedParams(columnsArray[0], columnsArray[1]);
        }
    }

    private void validateSingleParam(String param) {
        if (!ConsUtils.PARAMS_FOR_ARTICLES_ON_CART.contains(param)) {
            throw new InvalidRequestParam(INVALID_SORT_CRITERIA);
        }
    }

    private void validateCombinedParams(String param1, String param2) {
        if (!ConsUtils.COMBINED_PARAMS_FOR_ARTICLE.containsAll(List.of(param1, param2))) {
            throw new InvalidRequestParam(INVALID_SORT_CRITERIA);
        }
    }
}
