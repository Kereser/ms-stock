package com.emazon.ms_stock.domain.use_cases;

import com.emazon.ms_stock.ConsUtils;
import com.emazon.ms_stock.application.dto.*;
import com.emazon.ms_stock.domain.api.IArticleServicePort;
import com.emazon.ms_stock.domain.model.Article;
import com.emazon.ms_stock.domain.model.Brand;
import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.domain.spi.IArticlePersistencePort;
import com.emazon.ms_stock.domain.spi.IBrandPersistencePort;
import com.emazon.ms_stock.domain.spi.ICategoryPersistencePort;
import com.emazon.ms_stock.infra.exception.ArticleCategoryQuantityException;
import com.emazon.ms_stock.infra.exception.NoDataFoundException;
import com.emazon.ms_stock.infra.exception.NotSufficientStock;
import com.emazon.ms_stock.infra.out.jpa.entity.ArticleEntity;
import org.springframework.data.domain.Sort;

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
    public void save(Article entity) {
        List<Category> categories = categoryPersistencePort.findAllById(entity.getCategories().stream().map(Category::getId).toList());

        if (categories.size() < entity.getCategories().size()) {
            throw new NoDataFoundException(Article.class.getSimpleName(), ArticleReqDTO.Fields.categoryIds);
        }

        Optional<Brand> brand = brandPersistencePort.findById(entity.getBrand().getId());

        if (brand.isEmpty()) {
            throw new NoDataFoundException(Article.class.getSimpleName(), ArticleReqDTO.Fields.brandId);
        }

        entity.setCategories(new HashSet<>(categories));
        entity.setBrand(brand.get());

        persistencePort.save(entity);
    }

    @Override
    public PageDTO<Article> findAllPageable(PageHandler pageable) {
        if (pageable.getColumn().equalsIgnoreCase(Article.INNER_SORT_CATEGORY_NAME)) {
            if (pageable.getDirection().equalsIgnoreCase(Sort.Direction.ASC.toString())) {
                return persistencePort.findAllByCategoryNameAsc(pageable);
            }

            return persistencePort.findAllByCategoryNameDesc(pageable);
        }

        return persistencePort.findAllPageable(pageable);
    }

    @Override
    public void addSupply(Set<ItemQuantityDTO> itemQuantityDTOS) {
        Map<Long, Long> articleQuantityMap = getArticleQuantityMap(itemQuantityDTOS);
        Set<Article> articlesFound = new HashSet<>(persistencePort.findAllById(articleQuantityMap.keySet()));

        handleNotFoundId(articleQuantityMap.keySet().size(), articlesFound.size());
        addQuantityToArticle(articleQuantityMap, articlesFound);

        persistencePort.saveAll(articlesFound);
    }

    @Override
    public void handleCartAdditionValidations(Set<ItemQuantityDTO> itemQuantityDTOS) {
        Map<Long, Long> articleQuantityMap = getArticleQuantityMap(itemQuantityDTOS);
        Set<Article> articlesFound = new HashSet<>(persistencePort.findAllById(articleQuantityMap.keySet()));

        handleNotFoundId(articleQuantityMap.keySet().size(), articlesFound.size());
        handleInsufficientStock(articlesFound, articleQuantityMap);
        handleArticleCategoryQuantityConstraint(articlesFound);
    }

    private void addQuantityToArticle(Map<Long, Long> articleQuantityMap, Set<Article> articlesFound) {
        articlesFound.forEach(a -> {
            Long quantity = articleQuantityMap.get(a.getId());
            a.addQuantityBySupply(quantity);
        });
    }

    private Map<Long, Long> getArticleQuantityMap(Set<ItemQuantityDTO> itemQuantityDTOS) {
        return itemQuantityDTOS.stream().collect(Collectors.toMap(ItemQuantityDTO::getArticleId, ItemQuantityDTO::getQuantity));
    }

    private void handleNotFoundId(int fromReq, int fromDB) {
        if (fromDB != fromReq) {
            throw new NoDataFoundException(Article.class.getSimpleName(), ArticleEntity.Fields.id);
        }
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

        Map<Long, Integer> hashMap = buildHashMap(articles);

        if (hashMap.values().stream().anyMatch(c -> c > ConsUtils.INTEGER_3)) {
            throw new ArticleCategoryQuantityException(Article.class.getSimpleName(), ArticleEntity.Fields.categories);
        }
    }

    private Map<Long, Integer> buildHashMap(Set<Article> articles) {
        Map<Long, Integer> hashMap = new HashMap<>();
        List<Long> categoriesId = new ArrayList<>();

        articles.forEach(a -> categoriesId.addAll(a.getCategories().stream().map(Category::getId).toList()));
        categoriesId.forEach(cId -> hashMap.compute(cId, (k, v) -> v == null ? ConsUtils.INTEGER_1 : v + ConsUtils.INTEGER_1));

        return hashMap;
    }
}
