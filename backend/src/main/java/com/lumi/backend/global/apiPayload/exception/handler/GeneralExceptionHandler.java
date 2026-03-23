package com.lumi.backend.global.apiPayload.exception.handler;

import com.lumi.backend.global.apiPayload.ApiResponse;
import com.lumi.backend.global.apiPayload.code.GeneralErrorCode;
import com.lumi.backend.global.apiPayload.exception.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(GeneralException e) {
        log.warn("GeneralException: {}", e.getMessage());
        ApiResponse<Void> response = ApiResponse.onFailure(e.getErrorCode(), null);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, List<String>>>> handleValidationException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        String message = fieldErrors.isEmpty()
                ? GeneralErrorCode.BAD_REQUEST.getMessage()
                : fieldErrors.get(0).getDefaultMessage();

        Map<String, List<String>> fieldErrorMap = fieldErrors.stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));

        log.warn("ValidationException: {}", message);

        ApiResponse<Map<String, List<String>>> response = ApiResponse.<Map<String, List<String>>>builder()
                .isSuccess(false)
                .code(GeneralErrorCode.BAD_REQUEST.getCode())
                .message(message)
                .result(fieldErrorMap.isEmpty() ? null : fieldErrorMap)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("HttpMessageNotReadableException: {}", e.getMessage());
        ApiResponse<Void> response = ApiResponse.onFailure(GeneralErrorCode.BAD_REQUEST, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Unhandled exception: {}", e.getMessage(), e);
        ApiResponse<Void> response = ApiResponse.onFailure(GeneralErrorCode.INTERNAL_SERVER_ERROR, null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
