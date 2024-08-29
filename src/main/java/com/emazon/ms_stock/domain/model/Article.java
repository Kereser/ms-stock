package com.emazon.ms_stock.domain.model;

import java.math.BigDecimal;
import java.util.Set;

public class Article {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Long quantity;
    private Set<Category> categories;
    private Brand brand;

    public enum SortBy {
        ARTICLE_NAME("articleName"),
        DESCRIPTION_NAME("descriptionName"),
        CATEGORY("category");

        private final String value;

        SortBy(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

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
}
