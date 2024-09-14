package com.emazon.ms_stock.infra.exception;

public class NoDataFoundException extends BaseEntityException {
    public NoDataFoundException(String entityName, String field) {
      super(entityName, field);
    }
}
