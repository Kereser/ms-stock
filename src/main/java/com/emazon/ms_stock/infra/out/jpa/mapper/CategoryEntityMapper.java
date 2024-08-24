package com.emazon.ms_stock.infra.out.jpa.mapper;

import com.emazon.ms_stock.application.dto.CategoryResDTO;
import com.emazon.ms_stock.application.dto.PageDTO;
import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.infra.out.jpa.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CategoryEntityMapper {
    CategoryEntity toEntity(Category category);

    Category toCategory(CategoryEntity categoryEntity);

    List<Category> toCategoryList(List<CategoryEntity> categoryEntityList);

    List<CategoryResDTO> toCategoryResDTOList(List<Category> categoryList);

    default Page<Category> toCategoryPage(Page<CategoryEntity> categoryEntityList) {
        List<Category> categoryList = toCategoryList(categoryEntityList.getContent());
        return new PageImpl<>(categoryList, categoryEntityList.getPageable(), categoryEntityList.getTotalElements());
    }

    default Page<CategoryResDTO> toCategoryResDTOPage(Page<Category> categoryList) {
        List<CategoryResDTO> dtoList = toCategoryResDTOList(categoryList.getContent());
        return new PageImpl<>(dtoList, categoryList.getPageable(), categoryList.getTotalElements());
    }

    PageDTO<CategoryResDTO> toPageDTO(Page<Category> categories);
}
