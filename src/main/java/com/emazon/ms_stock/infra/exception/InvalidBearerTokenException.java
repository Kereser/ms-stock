package com.emazon.ms_stock.infra.exception;

import com.emazon.ms_stock.infra.exception_handler.ExceptionResponse;
import org.springframework.security.core.AuthenticationException;

public class InvalidBearerTokenException extends AuthenticationException {
    public InvalidBearerTokenException() {
        super(ExceptionResponse.INVALID_TOKEN);
    }
}
