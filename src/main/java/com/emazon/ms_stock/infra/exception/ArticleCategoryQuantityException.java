package com.emazon.ms_stock.infra.exception;

public class ArticleCategoryQuantityException extends BaseEntityException {
    public ArticleCategoryQuantityException(String entity, String field) {
        super(entity, field);
    }
}
