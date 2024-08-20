package com.emazon.ms_stock.infra.exception_handler;

import com.emazon.ms_stock.infra.exception.BaseEntityException;
import com.emazon.ms_stock.infra.exception.CategoryAlreadyExists;
import com.emazon.ms_stock.infra.exception.CategoryFieldConstraint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor {
    private static final String MESSAGE = "Message";
    private static final String REASON = "reason";

    @ExceptionHandler(CategoryAlreadyExists.class)
    public ResponseEntity<Map<String, String>> handleEntityAlreadyExists(BaseEntityException exception) {
        String msg = exception.getEntityName() + ExceptionResponse.ENTITY_ALREADY_EXISTS.getMessage();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(MESSAGE, msg));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleFieldValidations(MethodArgumentNotValidException ex) {
        Map<String, Object> res = new HashMap<>();
        res.put(MESSAGE, "Request has field validation errors");

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> fieldErrors.put(e.getField(), e.getDefaultMessage()));

        res.put("Field errors", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
