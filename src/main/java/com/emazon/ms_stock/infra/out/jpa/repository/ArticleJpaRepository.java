package com.emazon.ms_stock.infra.out.jpa.repository;

import com.emazon.ms_stock.ConsUtils;
import com.emazon.ms_stock.infra.out.jpa.entity.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface ArticleJpaRepository extends JpaRepository<ArticleEntity, Long> {
    Optional<ArticleEntity> findByName(String name);

    @Query("""
        SELECT a FROM article a
        WHERE (:articleIds IS NULL OR a.id IN :articleIds)
    """)
    Page<ArticleEntity> findAllByArticleIdPageable(Pageable pageable, @Param(ConsUtils.ARTICLE_IDS) Set<Long> articleIds);

    @Query("""
        SELECT a FROM article a
        JOIN a.categories c
        WHERE (:articleIds IS NULL OR a.id IN :articleIds)
        GROUP BY a ORDER BY MIN(c.name) ASC
    """)
    Page<ArticleEntity> findAllArticlesOrderByCategoryNameASC(Pageable pageable, @Param(ConsUtils.ARTICLE_IDS) Set<Long> articleIds);

    @Query("""
        SELECT a FROM article a
        JOIN a.categories c
        WHERE (:articleIds IS NULL OR a.id IN :articleIds)
        GROUP BY a ORDER BY MIN(c.name) DESC
    """)
    Page<ArticleEntity> findAllArticlesOrderByCategoryNameDESC(Pageable pageable, @Param(ConsUtils.ARTICLE_IDS) Set<Long> articleIds);

    @Query("""
        SELECT a FROM article a
        JOIN a.categories c
        JOIN a.brand b
        WHERE (:articleIds IS NULL OR a.id IN :articleIds)
        GROUP BY a
        ORDER BY MIN(c.name), b.name ASC
    """)
    Page<ArticleEntity> findAllByArticleIdsAndCategoryAndBrandNameAsc(Pageable pageable, @Param(ConsUtils.ARTICLE_IDS) Set<Long> articleIds);

    @Query("""
        SELECT a FROM article a
        JOIN a.categories c
        JOIN a.brand b
        WHERE (:articleIds IS NULL OR a.id IN :articleIds)
        GROUP BY a
        ORDER BY MIN(c.name), b.name DESC
    """)
    Page<ArticleEntity> findAllByArticleIdsAndCategoryAndBrandNameDesc(Pageable pageable, @Param(ConsUtils.ARTICLE_IDS) Set<Long> articleIds);
}
