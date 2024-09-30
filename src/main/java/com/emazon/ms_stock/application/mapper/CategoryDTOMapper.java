package com.emazon.ms_stock.application.mapper;

import com.emazon.ms_stock.application.dto.out.CategoryReqDTO;
import com.emazon.ms_stock.application.dto.out.CategoryResDTO;
import com.emazon.ms_stock.application.dto.handlers.PageDTO;
import com.emazon.ms_stock.domain.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CategoryDTOMapper {
    Category toCategory(CategoryReqDTO dto);
    CategoryResDTO toResDTO(Category article);
    PageDTO<CategoryResDTO> toPageResDTO(PageDTO<Category> pageArticle);
}
