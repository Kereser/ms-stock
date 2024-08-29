package com.emazon.ms_stock.infra.out.jpa.mapper;

import com.emazon.ms_stock.application.dto.BrandReqDTO;
import com.emazon.ms_stock.application.dto.BrandResDTO;
import com.emazon.ms_stock.application.dto.PageDTO;
import com.emazon.ms_stock.domain.model.Brand;
import com.emazon.ms_stock.infra.out.jpa.entity.BrandEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface BrandEntityMapper {
    Brand toBrand(BrandEntity entity);
    Brand toBrand(BrandReqDTO dto);
    BrandEntity toEntity(Brand brand);

    default Brand toBrandFromId(Long id) {
        if (id == null) {
            return null;
        }

        Brand br = new Brand();
        br.setId(id);
        return br;
    }

    @Mapping(source = "number", target = "currentPage")
    PageDTO<BrandEntity> toEntityPage(Page<BrandEntity> entityPages);
    PageDTO<Brand> toBrandPage(PageDTO<BrandEntity> entityPages);
    PageDTO<BrandResDTO> toBrandResPage(PageDTO<Brand> brandPages);
}
