package com.emazon.ms_stock;

import com.emazon.ms_stock.application.dto.ItemQuantityDTO;
import com.emazon.ms_stock.domain.model.Article;
import com.emazon.ms_stock.domain.model.Brand;
import com.emazon.ms_stock.domain.model.Category;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class TestCreationUtils {
    private TestCreationUtils() {
    }

    public static final String ARTICLE_NAME_PREFIX = "ArticleName";
    public static final String ARTICLE_DESCRIPTION_PREFIX = "ArticleDescription";
    public static final Long INITIAL_QUANTITY = 10L;

    private static Long articleQuantityCounter = INITIAL_QUANTITY;
    private static Integer nameCounter = 1;

    public static final String CATEGORY_NAME_PREFIX = "CategoryName";
    public static final String CATEGORY_DESCRIPTION_PREFIX = "CategoryDescription";

    private static Integer categoryNameCounter = 1;

    public static final String BRAND_NAME_PREFIX = "BrandName";
    public static final String BRAND_DESCRIPTION_PREFIX = "BrandDescription";

    private static Integer brandNameCounter = 1;

    public static Brand createBrand() {
        return new Brand(
                BRAND_NAME_PREFIX + brandNameCounter,
                BRAND_DESCRIPTION_PREFIX + brandNameCounter++
        );
    }

    public static Category createCategory() {
        return new Category(
                CATEGORY_NAME_PREFIX + categoryNameCounter,
                CATEGORY_DESCRIPTION_PREFIX + categoryNameCounter++
        );
    }

    public static Article createArticle() {
        return new Article(
                ConsUtils.LONG_1,
                ARTICLE_NAME_PREFIX + nameCounter,
                ARTICLE_DESCRIPTION_PREFIX + nameCounter++,
                BigDecimal.TEN,
                articleQuantityCounter++,
                new HashSet<>(Set.of(createCategory())),
                createBrand()
        );
    }

    public static ItemQuantityDTO createItemQuantity() {
        return ItemQuantityDTO.builder()
                .articleId(ConsUtils.LONG_1)
                .quantity(ConsUtils.LONG_1)
                .build();
    }
}
