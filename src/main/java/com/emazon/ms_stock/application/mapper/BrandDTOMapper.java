package com.emazon.ms_stock.application.mapper;


import com.emazon.ms_stock.application.dto.input.BrandReqDTO;
import com.emazon.ms_stock.application.dto.out.BrandResDTO;
import com.emazon.ms_stock.application.dto.handlers.PageDTO;
import com.emazon.ms_stock.domain.model.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface BrandDTOMapper {
    Brand toBrand(BrandReqDTO dto);
    BrandResDTO toResDTO(Brand article);
    PageDTO<BrandResDTO> toPageResDTO(PageDTO<Brand> pageArticle);
}
