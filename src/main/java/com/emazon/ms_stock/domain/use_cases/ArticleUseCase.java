package com.emazon.ms_stock.domain.use_cases;

import com.emazon.ms_stock.application.dto.ArticleReqDTO;
import com.emazon.ms_stock.application.dto.PageDTO;
import com.emazon.ms_stock.application.dto.PageHandler;
import com.emazon.ms_stock.application.dto.supply.SupplyReqDTO;
import com.emazon.ms_stock.domain.api.IArticleServicePort;
import com.emazon.ms_stock.domain.model.Article;
import com.emazon.ms_stock.domain.model.Brand;
import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.domain.spi.IArticlePersistencePort;
import com.emazon.ms_stock.domain.spi.IBrandPersistencePort;
import com.emazon.ms_stock.domain.spi.ICategoryPersistencePort;
import com.emazon.ms_stock.infra.exception.NoDataFoundException;
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
    public void addSupply(Set<SupplyReqDTO> dto) {
        Map<Long, Long> articleQuantityMap = dto.stream().collect(Collectors.toMap(SupplyReqDTO::getArticleId, SupplyReqDTO::getQuantity));

        Set<Article> articlesFound = new HashSet<>(persistencePort.findAllById(articleQuantityMap.keySet()));

        if (articleQuantityMap.keySet().size() != articlesFound.size()) {
            throw new NoDataFoundException(Article.class.getSimpleName(), ArticleEntity.Fields.id);
        }

        articlesFound.forEach(a -> {
            Long quantity = articleQuantityMap.get(a.getId());
            a.addQuantityBySupply(quantity);
        });

        persistencePort.saveAll(articlesFound);
    }
}
