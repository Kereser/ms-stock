package com.emazon.ms_stock.infra.out.jpa.mapper;

import com.emazon.ms_stock.application.dto.BrandReqDTO;
import com.emazon.ms_stock.domain.model.Brand;
import com.emazon.ms_stock.infra.out.jpa.entity.BrandEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface BrandEntityMapper {
    Brand toBrand(BrandEntity entity);
    Brand toBrand(BrandReqDTO dto);
    BrandEntity toEntity(Brand brand);
    List<Brand> toBrandList(List<BrandEntity> entityList);
    List<BrandEntity> toEntityList(List<Brand> brandList);
}
