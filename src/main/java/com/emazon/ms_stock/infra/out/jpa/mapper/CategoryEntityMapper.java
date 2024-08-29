package com.emazon.ms_stock.infra.out.jpa.mapper;

import com.emazon.ms_stock.application.dto.CategoryResDTO;
import com.emazon.ms_stock.application.dto.PageDTO;
import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.infra.out.jpa.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CategoryEntityMapper {
    CategoryEntity toEntity(Category category);

    default Category toCategoryFromId(Long id) {
        if (id == null) {
            return null;
        }

        Category cg = new Category();
        cg.setId(id);
        return cg;
    }

    Category toCategory(CategoryEntity categoryEntity);

    List<Category> toCategoryList(List<CategoryEntity> categoryEntityList);

    List<CategoryResDTO> toCategoryResDTOList(List<Category> categoryList);

    @Mapping(source = "number", target = "currentPage")
    PageDTO<CategoryEntity> toEntityPage(Page<CategoryEntity> entityPages);
    PageDTO<Category> toCategoryPage(PageDTO<CategoryEntity> entityPages);
    PageDTO<CategoryResDTO> toCategoryRes(PageDTO<Category> categoryPages);
}
