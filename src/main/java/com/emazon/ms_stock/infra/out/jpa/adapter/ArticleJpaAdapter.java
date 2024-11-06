package com.emazon.ms_stock.infra.out.jpa.adapter;

import com.emazon.ms_stock.ConsUtils;
import com.emazon.ms_stock.application.dto.handlers.PageDTO;
import com.emazon.ms_stock.application.dto.handlers.PageHandler;
import com.emazon.ms_stock.application.dto.out.ArticlesPriceDTO;
import com.emazon.ms_stock.application.utils.ParsingUtils;
import com.emazon.ms_stock.domain.model.Article;
import com.emazon.ms_stock.domain.spi.IArticlePersistencePort;
import com.emazon.ms_stock.infra.out.jpa.entity.ArticleEntity;
import com.emazon.ms_stock.infra.out.jpa.mapper.ArticleEntityMapper;
import com.emazon.ms_stock.infra.out.jpa.repository.ArticleJpaRepository;
import com.emazon.ms_stock.infra.out.jpa.repository.sort.ArticleSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ArticleJpaAdapter implements IArticlePersistencePort {

    private final ArticleJpaRepository repository;
    private final ArticleEntityMapper mapper;

    @Override
    public void save(Article entity) {
        repository.save(mapper.toEntity(entity));
    }

    @Override
    public PageDTO<Article> findAll(PageHandler page) {
        Pageable pageable = ParsingUtils.toPageableUnsorted(page);
        Specification<ArticleEntity> spec = buildSpecWithFiltersAndOrderBy(page);

        PageDTO<ArticleEntity> pageEntity = mapper.articleEntityPageToArticleEntityPageDTO(repository.findAll(spec, pageable));
        return mapper.articleEntityPageDTOToArticlePageDTO(pageEntity);
    }

    private Specification<ArticleEntity> buildSpecWithFiltersAndOrderBy(PageHandler page) {
        Set<Long> articleIds = buildArticleIds(page.getFilters().getOrDefault(ConsUtils.ARTICLE_IDS, null));

        return ArticleSpecification.orderBy(page.getColumn(), page.getDirection().equalsIgnoreCase(ConsUtils.ASC))
                .and(ArticleSpecification.hasArticleIds(articleIds))
                .and(ArticleSpecification.hasBrandName(page.getFilters().getOrDefault(ConsUtils.BRAND_NAME, null)))
                .and(ArticleSpecification.hasCategoryName(page.getFilters().getOrDefault(ConsUtils.CATEGORY_NAME, null)));
    }

    private Set<Long> buildArticleIds(String articleIds) {
        if (articleIds == null || articleIds.isEmpty()) return new HashSet<>();

        return Arrays.asList(articleIds.split(ConsUtils.COMMA_DELIMITER))
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }

    @Override
    public List<Article> findAllById(Iterable<Long> ids) {
        return mapper.toList(repository.findAllById(ids));
    }

    @Override
    public void saveAll(Iterable<Article> articles) {
        repository.saveAll(mapper.articlesToArticleEntities(articles));
    }

    @Override
    public Set<ArticlesPriceDTO> getArticlesPrice(Set<Long> articleIds) {
        return mapper.articleEntitiesToArticlesPriceDTO(repository.findAllById(articleIds));
    }
}
