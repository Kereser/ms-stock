package com.emazon.ms_stock.infra.exception;

public class CategoryAlreadyExists extends BaseEntityException {
    public CategoryAlreadyExists() {
        super("Category", "Name");
    }
}
