package com.emazon.ms_stock.infra.exception;

import lombok.Getter;

@Getter
public class InvalidRequestParam extends RuntimeException {
    public InvalidRequestParam(String message) {
        super(message);
    }
}
