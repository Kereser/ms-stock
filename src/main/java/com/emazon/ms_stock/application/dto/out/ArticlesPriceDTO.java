package com.emazon.ms_stock.application.dto.out;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ArticlesPriceDTO {
    private Long id;
    private BigDecimal price;
    private Long quantity;
}
