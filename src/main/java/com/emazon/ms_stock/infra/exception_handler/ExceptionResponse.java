package com.emazon.ms_stock.infra.exception_handler;

import lombok.Getter;

@Getter
public enum ExceptionResponse {
    ENTITY_ALREADY_EXISTS(" already exists"),
    FIELD_CONSTRAINT_VIOLATION(" has field constraint violation");

    private final String message;

    ExceptionResponse(String msg) {
        message = msg;
    }
}
