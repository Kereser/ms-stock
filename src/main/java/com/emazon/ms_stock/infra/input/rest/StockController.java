package com.emazon.ms_stock.infra.input.rest;

import com.emazon.ms_stock.application.dto.BrandReqDTO;
import com.emazon.ms_stock.application.dto.CategoryReqDTO;
import com.emazon.ms_stock.application.dto.CategoryResDTO;
import com.emazon.ms_stock.application.dto.PageDTO;
import com.emazon.ms_stock.application.handler.IStockHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final IStockHandler stockHandler;

    @PostMapping("/categories")
    public ResponseEntity<Void> createCategory(@RequestBody @Valid CategoryReqDTO reqDTO) {
        stockHandler.saveCategoryInStock(reqDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/categories")
    public ResponseEntity<PageDTO<CategoryResDTO>> getAllCategories(
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer page) {
        return ResponseEntity.ok().body(stockHandler.getAllCategories(direction, pageSize, page));
    }

    @PostMapping("/brands")
    public ResponseEntity<Void> createBrand(@RequestBody @Valid BrandReqDTO dto) {
        stockHandler.createBrandInStock(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
