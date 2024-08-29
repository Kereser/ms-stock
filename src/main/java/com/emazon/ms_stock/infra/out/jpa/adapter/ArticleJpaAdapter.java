package com.emazon.ms_stock.infra.out.jpa.adapter;

import com.emazon.ms_stock.application.dto.PageDTO;
import com.emazon.ms_stock.application.dto.PageHandler;
import com.emazon.ms_stock.domain.model.Article;
import com.emazon.ms_stock.domain.spi.IArticlePersistencePort;
import com.emazon.ms_stock.infra.out.jpa.entity.ArticleEntity;
import com.emazon.ms_stock.infra.out.jpa.mapper.ArticleEntityMapper;
import com.emazon.ms_stock.infra.out.jpa.repository.ArticleJpaRepository;
import lombok.RequiredArgsConstructor;

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
    public void update(Article entity) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public PageDTO<Article> findAllPageable(PageHandler page) {
        return null;
    }

    @Override
    public Optional<Article> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Article> findByName(String name) {
        Optional<ArticleEntity> opt = repository.findByName(name);
        return opt.map(mapper::toArticle);
    }
}
