package com.emazon.ms_stock.infra.out.jpa.mapper;

import com.emazon.ms_stock.application.dto.handlers.PageDTO;
import com.emazon.ms_stock.application.dto.out.ArticlesPriceDTO;
import com.emazon.ms_stock.domain.model.Article;
import com.emazon.ms_stock.infra.out.jpa.entity.ArticleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring",
        uses = {CategoryEntityMapper.class, BrandEntityMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ArticleEntityMapper {
    ArticleEntity toEntity(Article article);

    Article toArticle(ArticleEntity entity);

    @Mapping(source = "number", target = "currentPage")
    PageDTO<ArticleEntity> articleEntityPageToArticleEntityPageDTO(Page<ArticleEntity> articleEntityPage);

    PageDTO<Article> articleEntityPageDTOToArticlePageDTO(PageDTO<ArticleEntity> articleEntityPageDTO);

    List<Article> toList(List<ArticleEntity> entityList);

    Iterable<ArticleEntity> articlesToArticleEntities(Iterable<Article> articles);

    default Set<ArticlesPriceDTO> articleEntitiesToArticlesPriceDTO(List<ArticleEntity> articleEntities) {
        if (articleEntities == null) return new HashSet<>();

        Set<ArticlesPriceDTO> articlesPriceDTOS = new HashSet<>();

        articleEntities.forEach(a -> {
            ArticlesPriceDTO articleDTO = new ArticlesPriceDTO();

            articleDTO.setId(a.getId());
            articleDTO.setPrice(a.getPrice());
            articleDTO.setQuantity(a.getQuantity());

            articlesPriceDTOS.add(articleDTO);
        });

        return articlesPriceDTOS;
    }
}
