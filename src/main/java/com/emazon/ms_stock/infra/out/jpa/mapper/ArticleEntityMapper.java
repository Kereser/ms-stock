package com.emazon.ms_stock.infra.out.jpa.mapper;

import com.emazon.ms_stock.application.dto.ArticleReqDTO;
import com.emazon.ms_stock.domain.model.Article;
import com.emazon.ms_stock.infra.out.jpa.entity.ArticleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = {CategoryEntityMapper.class, BrandEntityMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ArticleEntityMapper {
    ArticleEntity toEntity(Article article);

    @Mapping(source = "categoryIds", target = "categories")
    @Mapping(source = "brandId", target = "brand")
    Article toArticle(ArticleReqDTO dto);

    Article toArticle(ArticleEntity entity);
}
