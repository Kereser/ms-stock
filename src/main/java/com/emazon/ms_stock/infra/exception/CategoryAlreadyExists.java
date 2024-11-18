package com.emazon.ms_stock.infra.exception;

import com.emazon.ms_stock.ConsUtils;

public class CategoryAlreadyExists extends BaseEntityException {
  public CategoryAlreadyExists() {
    super("Category", ConsUtils.NAME);
  }
}
