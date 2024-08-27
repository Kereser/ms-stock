package com.emazon.ms_stock.domain.model;

public class Brand {
    private Long id;
    private String name;
    private String description;


    public enum SortBy {
        NAME("name"),
        DESCRIPTION("description");

        private final String value;

        SortBy(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public Brand(String name, String description) {
        this.name = name;
        this.description = description;
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
}
