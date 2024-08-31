package com.emazon.ms_stock.application.mapper;

import com.emazon.ms_stock.application.dto.ArticleReqDTO;
import com.emazon.ms_stock.application.dto.ArticleResDTO;
import com.emazon.ms_stock.application.dto.PageDTO;
import com.emazon.ms_stock.domain.model.Article;
import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.infra.out.jpa.mapper.BrandEntityMapper;
import com.emazon.ms_stock.infra.out.jpa.mapper.CategoryEntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = {CategoryEntityMapper.class, BrandEntityMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ArticleDTOMapper {
    @Mapping(source = "categoryIds", target = "categories")
    @Mapping(source = "brandId", target = "brand")
    Article toArticle(ArticleReqDTO dto);

    ArticleResDTO toResDTO(Article article);
    PageDTO<ArticleResDTO> toPageResDTO(PageDTO<Article> pageArticle);

    default ArticleResDTO.CategoryArticleRes map(Category category) {
        if (category == null) return null;

        ArticleResDTO.CategoryArticleRes categoryArticleRes = new ArticleResDTO.CategoryArticleRes();
        categoryArticleRes.setId(category.getId());
        categoryArticleRes.setName(category.getName());

        return categoryArticleRes;
    }
}
