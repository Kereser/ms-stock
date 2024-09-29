package com.emazon.ms_stock.application.mapper;

import com.emazon.ms_stock.application.dto.input.ArticleReqDTO;
import com.emazon.ms_stock.application.dto.out.ArticleResDTO;
import com.emazon.ms_stock.application.dto.out.CategoryArticleResDTO;
import com.emazon.ms_stock.application.dto.handlers.PageDTO;
import com.emazon.ms_stock.domain.model.Article;
import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.infra.out.jpa.mapper.BrandEntityMapper;
import com.emazon.ms_stock.infra.out.jpa.mapper.CategoryEntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {CategoryEntityMapper.class, BrandEntityMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ArticleDTOMapper {
    @Mapping(source = "categoryIds", target = "categories")
    @Mapping(source = "brandId", target = "brand")
    Article articleResDTOToArticle(ArticleReqDTO dto);

    List<ArticleResDTO> articlesToArticlesResDTOs(List<Article> article);

    PageDTO<ArticleResDTO> articlePageToArticleResPage(PageDTO<Article> pageArticle);

    default CategoryArticleResDTO map(Category category) {
        if (category == null) return null;

        CategoryArticleResDTO categoryArticleRes = new CategoryArticleResDTO();
        categoryArticleRes.setId(category.getId());
        categoryArticleRes.setName(category.getName());

        return categoryArticleRes;
    }
}
