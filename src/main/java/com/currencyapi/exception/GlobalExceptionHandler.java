package com.currencyapi.exception;

import com.currencyapi.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(
            MethodArgumentNotValidException ex) {
        Map<String, String> erreurs = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String champ = ((FieldError) error).getField();
            erreurs.put(champ, error.getDefaultMessage());
        });
        return ResponseEntity.badRequest()
                .body(ApiResponse.erreur("Erreurs de validation : " + erreurs));
    }

    @ExceptionHandler(DeviseInvalideException.class)
    public ResponseEntity<ApiResponse<Void>> handleDeviseInvalide(DeviseInvalideException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.erreur(ex.getMessage()));
    }

    @ExceptionHandler(ApiExterneException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiExterne(ApiExterneException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponse.erreur(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobal(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.erreur("Erreur interne : " + ex.getMessage()));
    }
}
