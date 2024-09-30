package com.emazon.ms_stock.domain.spi;

import com.emazon.ms_stock.application.dto.handlers.PageDTO;
import com.emazon.ms_stock.application.dto.out.ArticlesPriceDTO;
import com.emazon.ms_stock.domain.model.Article;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface IArticlePersistencePort extends IBasicCrudPersistencePort<Article> {
    PageDTO<Article> findAllByArticleIdPageable(Pageable page, Set<Long> articleIds);
    PageDTO<Article> findAllByCategoryNameAsc(Pageable page, Set<Long> articleIds);
    PageDTO<Article> findAllByCategoryNameDesc(Pageable page, Set<Long> articleIds);
    PageDTO<Article> findAllByArticleIdsAndCategoryAndBrandNameAsc(Pageable page, Set<Long> articleIds);
    PageDTO<Article> findAllByArticleIdsCategoryAndBrandNameDesc(Pageable page, Set<Long> articleIds);
    List<Article> findAllById(Iterable<Long> ids);
    void saveAll(Iterable<Article> articles);
    Set<ArticlesPriceDTO> getArticlesPrice(Set<Long> articleIds);
}
