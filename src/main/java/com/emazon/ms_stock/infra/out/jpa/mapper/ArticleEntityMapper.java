package com.emazon.ms_stock.infra.out.jpa.mapper;

import com.emazon.ms_stock.application.dto.PageDTO;
import com.emazon.ms_stock.domain.model.Article;
import com.emazon.ms_stock.infra.out.jpa.entity.ArticleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring",
        uses = {CategoryEntityMapper.class, BrandEntityMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ArticleEntityMapper {
    ArticleEntity toEntity(Article article);

    Article toArticle(ArticleEntity entity);

    @Mapping(source = "number", target = "currentPage")
    PageDTO<ArticleEntity> toArticleEntityPage(Page<ArticleEntity> articleEntityPage);

    PageDTO<Article> toArticlePage(PageDTO<ArticleEntity> articleEntityPageDTO);
}
