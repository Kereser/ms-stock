package com.emazon.ms_stock.infra.out.jpa.repository;

import com.emazon.ms_stock.infra.out.jpa.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ArticleJpaRepository extends JpaRepository<ArticleEntity, Long>, JpaSpecificationExecutor<ArticleEntity> {
    Optional<ArticleEntity> findByName(String name);
}
