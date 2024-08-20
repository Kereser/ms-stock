package com.emazon.ms_stock.infra.exception_handler;

import com.emazon.ms_stock.infra.exception.BaseEntityException;
import com.emazon.ms_stock.infra.exception.CategoryAlreadyExists;
import com.emazon.ms_stock.infra.exception.CategoryFieldConstraint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

    @ExceptionHandler(CategoryFieldConstraint.class)
    public ResponseEntity<Map<String, String>> handleFieldConstrainsViolantion(BaseEntityException ex) {
        String msg = ex.getEntityName() + ExceptionResponse.FIELD_CONSTRAINT_VIOLATION.getMessage();
        String reason = ex.getField() + " " + ex.getReason();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MESSAGE, msg, REASON, reason));
    }
}
