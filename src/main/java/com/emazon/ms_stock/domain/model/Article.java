package com.emazon.ms_stock.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class Article {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Long quantity;
    private Set<Category> categories;
    private Brand brand;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Article() {
    }

    public Article(Long id, String name, String description, BigDecimal price, Long quantity, Set<Category> categories, Brand brand) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.categories = categories;
        this.brand = brand;
    }

    public static final String INNER_SORT_CATEGORY_NAME = "category:name";
    public static final String NAME_SORT = "name";
    public static final String DESCRIPTION_SORT = "description";
    public static final List<String> VALID_SORT_FIELDS = List.of(NAME_SORT, DESCRIPTION_SORT, INNER_SORT_CATEGORY_NAME);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public void addQuantityBySupply(Long quantity) {
        if (quantity > 0) {
            this.quantity += quantity;
        }
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public static Boolean isValidSortField(String fields) {
        return VALID_SORT_FIELDS.contains(fields);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
