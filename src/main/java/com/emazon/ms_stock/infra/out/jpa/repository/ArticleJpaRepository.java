package com.emazon.ms_stock.infra.out.jpa.repository;

import com.emazon.ms_stock.infra.out.jpa.entity.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ArticleJpaRepository extends JpaRepository<ArticleEntity, Long> {
    Optional<ArticleEntity> findByName(String name);

    @Query("SELECT DISTINCT a FROM ArticleEntity a LEFT JOIN FETCH a.categories")
    Page<ArticleEntity> findAllBySortCriteria(Pageable pageable);

    @Query("""
        SELECT a FROM ArticleEntity a
        JOIN a.categories c
        GROUP BY a ORDER BY MIN(c.name) ASC
    """)
    Page<ArticleEntity> findAllArticlesOrderByCategoryNameASC(Pageable pageable);

    @Query("""
        SELECT a FROM ArticleEntity a
        JOIN a.categories c
        GROUP BY a ORDER BY MIN(c.name) DESC
    """)
    Page<ArticleEntity> findAllArticlesOrderByCategoryNameDESC(Pageable pageable);

}
