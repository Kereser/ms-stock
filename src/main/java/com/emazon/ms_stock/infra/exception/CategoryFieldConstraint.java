package com.emazon.ms_stock.infra.exception;

public class CategoryFieldConstraint extends BaseEntityException {
    public CategoryFieldConstraint(String field, String reason) {
        super("Category", field, reason);
    }
}
