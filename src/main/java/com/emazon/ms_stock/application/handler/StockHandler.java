package com.emazon.ms_stock.application.handler;

import com.emazon.ms_stock.application.dto.BrandReqDTO;
import com.emazon.ms_stock.application.dto.CategoryReqDTO;
import com.emazon.ms_stock.application.dto.CategoryResDTO;
import com.emazon.ms_stock.application.dto.PageDTO;
import com.emazon.ms_stock.domain.api.IBrandServicePort;
import com.emazon.ms_stock.domain.api.ICategoryServicePort;
import com.emazon.ms_stock.domain.model.Brand;
import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.infra.out.jpa.mapper.BrandEntityMapper;
import com.emazon.ms_stock.infra.out.jpa.mapper.CategoryEntityMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StockHandler implements IStockHandler {

    private final ICategoryServicePort categoryServicePort;
    private final CategoryEntityMapper categoryEntityMapper;

    private final IBrandServicePort brandServicePort;
    private final BrandEntityMapper brandEntityMapper;

    @Override
    public void saveCategoryInStock(CategoryReqDTO reqDTO) {
        Category category = new Category(reqDTO.getName(), reqDTO.getDescription());

        categoryServicePort.save(category);
    }

    @Override
    public PageDTO<CategoryResDTO> getAllCategories(String direction, Integer pageSize, Integer pageNumber) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by("name").descending() : Sort.by("name").ascending();
        Page<Category> categoryPage = categoryServicePort.findAllPageable(PageRequest.of(pageNumber, pageSize, sort));

        return categoryEntityMapper.toPageDTO(categoryPage);
    }

    @Override
    public void createBrandInStock(BrandReqDTO dto) {
        Brand brand = brandEntityMapper.toBrand(dto);
        brandServicePort.save(brand);
    }
}
