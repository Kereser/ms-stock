package com.emazon.ms_stock.infra.exception_handler;

import com.emazon.ms_stock.infra.exception.BaseEntityException;
import com.emazon.ms_stock.infra.exception.CategoryAlreadyExists;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor {
    private ExceptionResponse res;

    @ExceptionHandler(CategoryAlreadyExists.class)
    public ResponseEntity<ExceptionResponse> handleEntityAlreadyExists(BaseEntityException exception) {
        String msg = exception.getEntityName() + " already exists";
        res = ExceptionResponse.builder()
                .message(msg)
                .fieldErrors(Map.of(exception.getField(), "must be unique"))
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleFieldValidations(MethodArgumentNotValidException ex) {
        Map<String, Object> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(e -> fieldErrors.put(e.getField(), e.getDefaultMessage()));

        res = ExceptionResponse.builder()
                .message("Request has field validation errors")
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
