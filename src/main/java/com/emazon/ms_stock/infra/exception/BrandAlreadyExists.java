package com.emazon.ms_stock.infra.exception;

import com.emazon.ms_stock.ConsUtils;

public class BrandAlreadyExists extends BaseEntityException {
  public BrandAlreadyExists() {
    super("Brand", ConsUtils.NAME);
  }
}
