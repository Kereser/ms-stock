package com.emazon.ms_stock.application.dto.out;

import com.emazon.ms_stock.domain.model.Brand;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class ArticleResDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Long quantity;
    private Set<CategoryArticleResDTO> categories;
    private Brand brand;
    private LocalDateTime updatedAt;
}
