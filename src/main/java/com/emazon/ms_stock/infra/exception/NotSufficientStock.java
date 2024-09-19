package com.emazon.ms_stock.infra.exception;

public class NotSufficientStock extends BaseEntityException {
    public NotSufficientStock(String entity, String field, String reason) {
        super(entity, field, reason);
    }
}
