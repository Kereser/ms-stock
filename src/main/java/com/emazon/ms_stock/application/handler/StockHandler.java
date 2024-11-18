package com.emazon.ms_stock.application.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.emazon.ms_stock.ConsUtils;
import com.emazon.ms_stock.application.dto.ItemQuantityDTO;
import com.emazon.ms_stock.application.dto.ItemsReqDTO;
import com.emazon.ms_stock.application.dto.handlers.PageDTO;
import com.emazon.ms_stock.application.dto.handlers.PageHandler;
import com.emazon.ms_stock.application.dto.input.ArticleReqDTO;
import com.emazon.ms_stock.application.dto.input.BrandReqDTO;
import com.emazon.ms_stock.application.dto.out.*;
import com.emazon.ms_stock.application.mapper.ArticleDTOMapper;
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

@Service
@Transactional
@RequiredArgsConstructor
public class StockHandler implements IStockHandler {

  private static final String INVALID_SORT_CRITERIA = "Invalid sort criteria in request param";
  private final ICategoryServicePort categoryServicePort;
  private final CategoryDTOMapper categoryDTOMapper;
  private final IBrandServicePort brandServicePort;
  private final BrandDTOMapper brandDTOMapper;
  private final IArticleServicePort articleServicePort;
  private final ArticleDTOMapper articleDTOMapper;

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

    PageDTO<Category> categoryPages = categoryServicePort
                                        .findAllPageable(buildPageHandler(direction, pageSize, page, column));
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

    PageDTO<Brand> brandPagesDTO = brandServicePort
                                     .findAllPageable(buildPageHandler(direction, pageSize, page, column));
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

    PageDTO<Article> articlePageDTO = articleServicePort
                                        .findAllPageable(buildPageHandler(direction, pageSize, page, column));
    return articleDTOMapper.articlePageToArticleResPage(articlePageDTO);
  }

  @Override
  public void addSupply(Set<ItemQuantityDTO> itemQuantityDTOS) {
    articleServicePort.addSupply(itemQuantityDTOS);
  }

  @Override
  public void validationsOnStockForCart(Set<ItemQuantityDTO> itemQuantityDTOS) {
    articleServicePort.validationsOnStockForCart(itemQuantityDTOS);
  }

  @Override
  public PageDTO<ArticleResDTO> getArticlesForCart(String direction, Integer pageSize, Integer page, String columns,
                                                   String categoryName, String brandName, String articleIds) {
    validColumnsToSort(columns);

    Map<String, String> filters = buildFilterMap(categoryName, brandName, articleIds);
    PageDTO<Article> articles = articleServicePort
                                  .findAllPageable(buildPageHandlerWithFilters(direction, pageSize, page, columns, filters));

    return articleDTOMapper.articlePageToArticleResPage(articles);
  }

  private Map<String, String> buildFilterMap(String categoryName, String brandName, String articleIds) {
    Map<String, String> map = new HashMap<>();

    map.putIfAbsent(ConsUtils.CATEGORY_NAME, categoryName);
    map.putIfAbsent(ConsUtils.BRAND_NAME, brandName);
    map.putIfAbsent(ConsUtils.ARTICLE_IDS, articleIds);

    return map;
  }

  @Override
  public Set<ArticlesPriceDTO> getArticlesPrice(Set<Long> articleIds) {
    return articleServicePort.getArticlesPrice(articleIds);
  }

  @Override
  public List<ArticleResDTO> getAllArticles(ItemsReqDTO itemsReqDTO) {
    List<Article> articles = articleServicePort
                               .getAllArticles(itemsReqDTO.getItems().stream().map(ItemQuantityDTO::getArticleId).toList());

    return articleDTOMapper.articlesToArticlesResDTOs(articles);
  }

  @Override
  public void processStockReduction(ItemsReqDTO itemsReqDTO) {
    articleServicePort.processStockReduction(itemsReqDTO);
  }

  @Override
  public void processRollback(ItemsReqDTO itemsReqDTO) {
    articleServicePort.processRollback(itemsReqDTO);
  }

  @Override
  public List<CategoryResDTO> getAllCategoriesByName(List<String> names) {
    List<Category> categoryList = categoryServicePort.findCategoriesByName(names);

    return categoryDTOMapper.categoryListToCategoryResDTO(categoryList);
  }

  @Override
  public List<BrandResDTO> getBrandsByName(List<String> names) {
    List<Brand> brandList = brandServicePort.findAllByNames(names);

    return brandDTOMapper.brandToBrandResDTO(brandList);
  }

  private PageHandler buildPageHandlerWithFilters(String direction, Integer pageSize, Integer page, String columns,
                                                  Map<String, String> filters) {
    return ParsingUtils.getHandlerFromParams(direction, pageSize, page, columns, filters);
  }

  private PageHandler buildPageHandler(String direction, Integer pageSize, Integer page, String column) {
    return ParsingUtils.getHandlerFromParams(direction, pageSize, page, column);
  }

  private void validColumnsToSort(String columns) {
    String[] columnsArray = columns.split(ConsUtils.COMMA_DELIMITER);

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
