package com.emazon.ms_stock.infra.exception;

import lombok.Getter;

@Getter
public abstract class BaseEntityException extends RuntimeException {

  private final String field;
  private final String entityName;
  private final String reason;

  BaseEntityException(String entityName, String field) {
    super();
    this.field = field;
    this.entityName = entityName;
    this.reason = null;
  }

  BaseEntityException(String entityName, String field, String reason) {
    super();
    this.field = field;
    this.reason = reason;
    this.entityName = entityName;
  }
}
