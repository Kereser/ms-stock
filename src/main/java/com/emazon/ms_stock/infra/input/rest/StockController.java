package com.emazon.ms_stock.infra.input.rest;

import com.emazon.ms_stock.application.dto.CategoryReqDTO;
import com.emazon.ms_stock.application.dto.CategoryResDTO;
import com.emazon.ms_stock.application.handler.IStockHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock/categories")
@RequiredArgsConstructor
public class StockController {

    private final IStockHandler stockHandler;

    @PostMapping
    public ResponseEntity<Void> createCategory(@RequestBody @Valid CategoryReqDTO reqDTO) {
        stockHandler.saveCategoryInStock(reqDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Page<CategoryResDTO>> getAllCategories(
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer page) {
        return ResponseEntity.ok().body(stockHandler.getAllCategories(direction, pageSize, page));
    }
}
