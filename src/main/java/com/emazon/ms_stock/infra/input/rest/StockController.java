package com.emazon.ms_stock.infra.input.rest;

import com.emazon.ms_stock.application.dto.*;
import com.emazon.ms_stock.application.dto.supply.SupplyReqDTO;
import com.emazon.ms_stock.application.handler.IStockHandler;
import com.emazon.ms_stock.infra.SortOrder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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
            @RequestParam(defaultValue = "ASC") SortOrder direction,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "name") String column) {
        return ResponseEntity.ok().body(stockHandler.getAllCategories(direction.name(), pageSize, page, column));
    }

    @PostMapping("/brands")
    public ResponseEntity<Void> createBrand(@RequestBody @Valid BrandReqDTO dto) {
        stockHandler.createBrandInStock(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/brands")
    public ResponseEntity<PageDTO<BrandResDTO>> getAllBrandsPaged(
            @RequestParam(defaultValue = "ASC") SortOrder direction,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "name") String column) {
        return ResponseEntity.ok().body(stockHandler.getAllBrands(direction.name(), pageSize, page, column));
    }

    @PostMapping("/articles")
    public ResponseEntity<Void> createArticle(@RequestBody @Valid ArticleReqDTO articleDTO) {
        stockHandler.createArticleInStock(articleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/articles")
    public ResponseEntity<PageDTO<ArticleResDTO>> getAllArticles(
            @RequestParam(defaultValue = "ASC") SortOrder direction,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "name") String column) {
        return ResponseEntity.ok().body(stockHandler.getAllArticles(direction.name(), pageSize, page, column));
    }

    @PostMapping("/articles/supply")
    public ResponseEntity<Void> addSupply(@RequestBody @Valid Set<@Valid SupplyReqDTO> dto) {
        stockHandler.addSupply(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
