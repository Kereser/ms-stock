package com.emazon.ms_stock.infra.out.jpa.adapter;

import com.emazon.ms_stock.application.dto.PageDTO;
import com.emazon.ms_stock.application.dto.PageHandler;
import com.emazon.ms_stock.application.utils.ParsingUtils;
import com.emazon.ms_stock.domain.model.Article;
import com.emazon.ms_stock.domain.spi.IArticlePersistencePort;
import com.emazon.ms_stock.infra.out.jpa.entity.ArticleEntity;
import com.emazon.ms_stock.infra.out.jpa.mapper.ArticleEntityMapper;
import com.emazon.ms_stock.infra.out.jpa.repository.ArticleJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ArticleJpaAdapter implements IArticlePersistencePort {

    private final ArticleJpaRepository repository;
    private final ArticleEntityMapper mapper;

    @Override
    public void save(Article entity) {
        repository.save(mapper.toEntity(entity));
    }

    @Override
    public PageDTO<Article> findAllPageable(PageHandler page) {
        PageDTO<ArticleEntity> pageEntity = mapper.toArticleEntityPage(repository.findAll(ParsingUtils.toPageable(page)));

        return mapper.toArticlePage(pageEntity);
    }

    @Override
    public PageDTO<Article> findAllByCategoryNameAsc(PageHandler page) {
        PageDTO<ArticleEntity> pageEntity = mapper.toArticleEntityPage(repository.findAllArticlesOrderByCategoryNameASC(ParsingUtils.toPageableUnsorted(page)));

        return mapper.toArticlePage(pageEntity);
    }

    @Override
    public PageDTO<Article> findAllByCategoryNameDesc(PageHandler page) {
        PageDTO<ArticleEntity> pageEntity = mapper.toArticleEntityPage(repository.findAllArticlesOrderByCategoryNameDESC(ParsingUtils.toPageableUnsorted(page)));

        return mapper.toArticlePage(pageEntity);
    }

    @Override
    public List<Article> findAllById(Iterable<Long> ids) {
        return mapper.toList(repository.findAllById(ids));
    }

    @Override
    public void saveAll(Iterable<Article> articles) {
        repository.saveAll(mapper.toListEntity(articles));
    }
}
