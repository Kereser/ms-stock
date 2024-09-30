package com.emazon.ms_stock.infra.exception_handler;

import com.emazon.ms_stock.infra.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor {
    private ExceptionResponse res;

    @ExceptionHandler({CategoryAlreadyExists.class, BrandAlreadyExists.class})
    public ResponseEntity<ExceptionResponse> handleEntityAlreadyExists(BaseEntityException exception) {
        String msg = exception.getEntityName() + ExceptionResponse.ENTITY_ALREADY_EXISTS;
        res = ExceptionResponse.builder()
                .message(msg)
                .fieldErrors(Map.of(exception.getField(), ExceptionResponse.UNIQUE_CONSTRAINT_VIOLATION))
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleFieldValidations(MethodArgumentNotValidException ex) {
        Map<String, Object> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(e -> fieldErrors.put(e.getField(), e.getDefaultMessage()));

        res = ExceptionResponse.builder()
                .message(ExceptionResponse.FIELD_VALIDATION_ERRORS)
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(InvalidRequestParam.class)
    public ResponseEntity<ExceptionResponse> handleInvalidRequestParam(InvalidRequestParam ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().message(ex.getMessage()).build());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleNotValidReqParam(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder()
                .message(ExceptionResponse.NOT_VALID_PARAM)
                .fieldErrors(Map.of(ex.getName(), ex.getValue() != null ? ex.getValue() : "")).build());
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNoDataFound(BaseEntityException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put(ex.getField(), ExceptionResponse.ID_NOT_FOUND);

        res = ExceptionResponse.builder()
                .message(ExceptionResponse.ERROR_PROCESSING_OPERATION + ex.getEntityName())
                .fieldErrors(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(NotSufficientStock.class)
    public ResponseEntity<ExceptionResponse> handleNotEnoghStock(BaseEntityException ex) {
        res = ExceptionResponse.builder()
                .message(ExceptionResponse.STOCK_WILL_BE_AVAILABLE_SOON)
                .fieldErrors(Map.of(ex.getField(), String.format(ExceptionResponse.NOT_ENOUGH_STOCK, ex.getReason())))
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
    }

    @ExceptionHandler(ArticleCategoryQuantityException.class)
    public ResponseEntity<ExceptionResponse> handleNotValidArticleCategoryConstraint(BaseEntityException ex) {
        res = ExceptionResponse.builder()
                .message(ExceptionResponse.FAILED_CONSTRAINT_FOR_ARTICLE_CATEGORY)
                .fieldErrors(Map.of(ex.getField(), ExceptionResponse.EXCEEDED))
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestOnConstraintsForRequest(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder().message(ex.getMessage().split(":")[0]).build());
    }
}
