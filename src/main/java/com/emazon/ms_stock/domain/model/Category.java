package com.emazon.ms_stock.domain.model;

public class Category {
    private Long id;
    private String name;
    private String description;

    public enum SortBy {
        NAME("name");

        private String value;

        SortBy(String value) {
            this.value = value;
        }
    }

    public Category() {
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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
}
