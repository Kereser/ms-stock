package com.emazon.ms_stock.infra.out.jpa.adapter;

import com.emazon.ms_stock.application.dto.handlers.PageDTO;
import com.emazon.ms_stock.application.dto.out.ArticlesPriceDTO;
import com.emazon.ms_stock.domain.model.Article;
import com.emazon.ms_stock.domain.spi.IArticlePersistencePort;
import com.emazon.ms_stock.infra.out.jpa.entity.ArticleEntity;
import com.emazon.ms_stock.infra.out.jpa.mapper.ArticleEntityMapper;
import com.emazon.ms_stock.infra.out.jpa.repository.ArticleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class ArticleJpaAdapter implements IArticlePersistencePort {

    private final ArticleJpaRepository repository;
    private final ArticleEntityMapper mapper;

    @Override
    public void save(Article entity) {
        repository.save(mapper.toEntity(entity));
    }

    @Override
    public PageDTO<Article> findAllByArticleIdPageable(Pageable page, Set<Long> articleIds) {
        PageDTO<ArticleEntity> pageEntity = mapper.articleEntityPageToArticleEntityPageDTO(repository.findAllByArticleIdPageable(page, articleIds));

        return mapper.articleEntityPageDTOToArticlePageDTO(pageEntity);
    }

    @Override
    public PageDTO<Article> findAllByCategoryNameAsc(Pageable page, Set<Long> articleIds) {
        PageDTO<ArticleEntity> pageEntity = mapper.articleEntityPageToArticleEntityPageDTO(repository.findAllArticlesOrderByCategoryNameASC(page, articleIds));

        return mapper.articleEntityPageDTOToArticlePageDTO(pageEntity);
    }

    @Override
    public PageDTO<Article> findAllByCategoryNameDesc(Pageable page, Set<Long> articleIds) {
        PageDTO<ArticleEntity> pageEntity = mapper.articleEntityPageToArticleEntityPageDTO(repository.findAllArticlesOrderByCategoryNameDESC(page, articleIds));

        return mapper.articleEntityPageDTOToArticlePageDTO(pageEntity);
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
    public PageDTO<Article> findAllByArticleIdsAndCategoryAndBrandNameAsc(Pageable page, Set<Long> articleIds) {
        PageDTO<ArticleEntity> articleEntityPageDTO = mapper.articleEntityPageToArticleEntityPageDTO(repository.findAllByArticleIdsAndCategoryAndBrandNameAsc(page, articleIds));
        return mapper.articleEntityPageDTOToArticlePageDTO(articleEntityPageDTO);
    }

    @Override
    public PageDTO<Article> findAllByArticleIdsCategoryAndBrandNameDesc(Pageable page, Set<Long> articleIds) {
        PageDTO<ArticleEntity> articleEntityPageDTO = mapper.articleEntityPageToArticleEntityPageDTO(repository.findAllByArticleIdsAndCategoryAndBrandNameDesc(page, articleIds));
        return mapper.articleEntityPageDTOToArticlePageDTO(articleEntityPageDTO);
    }

    @Override
    public Set<ArticlesPriceDTO> getArticlesPrice(Set<Long> articleIds) {
        return mapper.articleEntitiesToArticlesPriceDTO(repository.findAllById(articleIds));
    }
}
