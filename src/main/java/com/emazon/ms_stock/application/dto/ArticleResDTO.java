package com.emazon.ms_stock.application.dto;

import com.emazon.ms_stock.domain.model.Brand;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class ArticleResDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Long quantity;
    private Set<CategoryArticleRes> categories;
    private Brand brand;

    @Getter
    @Setter
    public static class CategoryArticleRes {
        private Long id;
        private String name;
    }
}
