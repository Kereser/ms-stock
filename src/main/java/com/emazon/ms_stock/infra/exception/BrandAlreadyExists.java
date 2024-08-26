package com.emazon.ms_stock.infra.exception;

public class BrandAlreadyExists extends BaseEntityException {
    public BrandAlreadyExists() {
        super("Brand", "name");
    }
}
