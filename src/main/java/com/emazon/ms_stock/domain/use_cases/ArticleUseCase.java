package com.emazon.ms_stock.domain.use_cases;

import com.emazon.ms_stock.ConsUtils;
import com.emazon.ms_stock.application.dto.*;
import com.emazon.ms_stock.application.dto.handlers.PageDTO;
import com.emazon.ms_stock.application.dto.handlers.PageHandler;
import com.emazon.ms_stock.application.dto.input.ArticleReqDTO;
import com.emazon.ms_stock.application.dto.out.ArticlesPriceDTO;
import com.emazon.ms_stock.domain.api.IArticleServicePort;
import com.emazon.ms_stock.domain.model.Article;
import com.emazon.ms_stock.domain.model.Brand;
import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.domain.model.sort.BasicSortStrategy;
import com.emazon.ms_stock.domain.model.sort.CategoryBrandNameStrategy;
import com.emazon.ms_stock.domain.model.sort.CategoryNameStrategy;
import com.emazon.ms_stock.domain.model.sort.SortingStrategy;
import com.emazon.ms_stock.domain.spi.IArticlePersistencePort;
import com.emazon.ms_stock.domain.spi.IBrandPersistencePort;
import com.emazon.ms_stock.domain.spi.ICategoryPersistencePort;
import com.emazon.ms_stock.infra.exception.ArticleCategoryQuantityException;
import com.emazon.ms_stock.infra.exception.NoDataFoundException;
import com.emazon.ms_stock.infra.exception.NotSufficientStock;
import com.emazon.ms_stock.infra.out.jpa.entity.ArticleEntity;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ArticleUseCase implements IArticleServicePort {

    private final IArticlePersistencePort persistencePort;
    private final ICategoryPersistencePort categoryPersistencePort;
    private final IBrandPersistencePort brandPersistencePort;

    public ArticleUseCase(IArticlePersistencePort persistencePort,
                          ICategoryPersistencePort categoryPersistencePort,
                          IBrandPersistencePort brandPersistencePort) {
        this.persistencePort = persistencePort;
        this.categoryPersistencePort = categoryPersistencePort;
        this.brandPersistencePort = brandPersistencePort;
    }

    @Override
    public void save(Article article) {
        completeArticle(article);
        updateArticle(article);

        persistencePort.save(article);
    }

    private void completeArticle(Article article) {
        article.setCategories(new HashSet<>(evaluateCategoriesToAssign(article)));
        article.setBrand(evaluateBrandToAssign(article));
    }

    private List<Category> evaluateCategoriesToAssign(Article article) {
        List<Category> categories = categoryPersistencePort.findAllById(article.getCategories().stream().map(Category::getId).toList());

        if (categories.size() < article.getCategories().size()) {
            throw new NoDataFoundException(Article.class.getSimpleName(), ArticleReqDTO.Fields.categoryIds);
        }

        return categories;
    }

    private Brand evaluateBrandToAssign(Article article) {
        Optional<Brand> brand = brandPersistencePort.findById(article.getBrand().getId());

        if (brand.isEmpty()) {
            throw new NoDataFoundException(Article.class.getSimpleName(), ArticleReqDTO.Fields.brandId);
        }

        return brand.get();
    }

    @Override
    public PageDTO<Article> findAllPageable(PageHandler pageable) {
        SortingStrategy sortingStrategy = determineSortingStrategy(pageable);

        return sortingStrategy.sort(pageable);
    }

    private SortingStrategy determineSortingStrategy(PageHandler pageable) {
        if (isSortByCategoryBrandName(pageable.getColumn())) {
            return new CategoryBrandNameStrategy(persistencePort);
        }

        if (isSortByCategoryName(pageable.getColumn())) {
            return new CategoryNameStrategy(persistencePort);
        }

        return new BasicSortStrategy(persistencePort);
    }

    private boolean isSortByCategoryName(String column) {
        return column.equalsIgnoreCase(ConsUtils.CATEGORY_PARAM_VALUE);
    }

    private boolean isSortByCategoryBrandName(String column) {
        return column.split(",").length == ConsUtils.INTEGER_2;
    }

    @Override
    public void addSupply(Set<ItemQuantityDTO> itemQuantityDTOS) {
        Map<Long, Long> articleQuantityMap = getArticleQuantityMap(itemQuantityDTOS);
        Set<Article> articlesFound = new HashSet<>(persistencePort.findAllById(articleQuantityMap.keySet()));

        handleNotFoundId(articleQuantityMap.keySet().size(), articlesFound.size());
        addQuantityToArticle(articleQuantityMap, articlesFound);

        persistencePort.saveAll(articlesFound);
    }

    private void addQuantityToArticle(Map<Long, Long> articleQuantityMap, Set<Article> articlesFound) {
        articlesFound.forEach(a -> {
            Long quantity = articleQuantityMap.get(a.getId());
            a.addQuantityBySupply(quantity);
            updateArticle(a);
        });
    }

    @Override
    public void handleCartAdditionValidations(Set<ItemQuantityDTO> itemQuantityDTOS) {
        Map<Long, Long> articleQuantityMap = getArticleQuantityMap(itemQuantityDTOS);
        Set<Article> articlesFound = new HashSet<>(persistencePort.findAllById(articleQuantityMap.keySet()));

        handleNotFoundId(articleQuantityMap.keySet().size(), articlesFound.size());
        handleInsufficientStock(articlesFound, articleQuantityMap);
        handleArticleCategoryQuantityConstraint(articlesFound);
    }

    private void handleInsufficientStock(Set<Article> articlesFound, Map<Long, Long> map) {
        articlesFound.forEach(a -> {
            if (a.getQuantity() < map.get(a.getId())) {
                throw new NotSufficientStock(Article.class.getSimpleName(), ArticleEntity.Fields.id, a.getId().toString());
            }
        });
    }

    private void handleArticleCategoryQuantityConstraint(Set<Article> articles) {
        if (articles.size() <= 3) return;

        Map<Long, Integer> hashMap = buildCategoryIdsHashMap(articles);

        if (hashMap.values().stream().anyMatch(c -> c > ConsUtils.INTEGER_3)) {
            throw new ArticleCategoryQuantityException(Article.class.getSimpleName(), ArticleEntity.Fields.categories);
        }
    }

    private Map<Long, Integer> buildCategoryIdsHashMap(Set<Article> articles) {
        Map<Long, Integer> hashMap = new HashMap<>();
        List<Long> categoriesId = new ArrayList<>();

        articles.forEach(a -> categoriesId.addAll(a.getCategories().stream().map(Category::getId).toList()));
        categoriesId.forEach(cId -> hashMap.compute(cId, (k, v) -> v == null ? ConsUtils.INTEGER_1 : v + ConsUtils.INTEGER_1));

        return hashMap;
    }

    private void updateArticle(Article articlesFound) {
        articlesFound.setUpdatedAt(LocalDateTime.now());
    }

    private Map<Long, Long> getArticleQuantityMap(Set<ItemQuantityDTO> itemQuantityDTOS) {
        return itemQuantityDTOS.stream().collect(Collectors.toMap(ItemQuantityDTO::getArticleId, ItemQuantityDTO::getQuantity));
    }

    private void handleNotFoundId(int fromReq, int fromDB) {
        if (fromDB != fromReq) {
            throw new NoDataFoundException(Article.class.getSimpleName(), ArticleEntity.Fields.id);
        }
    }

    @Override
    public Set<ArticlesPriceDTO> getArticlesPrice(Set<Long> articleIds) {
        return persistencePort.getArticlesPrice(articleIds);
    }
}
